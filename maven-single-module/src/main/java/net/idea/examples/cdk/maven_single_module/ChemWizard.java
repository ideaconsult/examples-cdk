package net.idea.examples.cdk.maven_single_module;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.idea.examples.cdk.maven_single_module.MainApp._option;

import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.aromaticity.CDKHueckelAromaticityDetector;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.io.iterator.IteratingMDLReader;
import org.openscience.cdk.qsar.DescriptorValue;
import org.openscience.cdk.qsar.descriptors.molecular.ALOGPDescriptor;
import org.openscience.cdk.qsar.descriptors.molecular.XLogPDescriptor;
import org.openscience.cdk.qsar.result.DoubleArrayResult;
import org.openscience.cdk.smiles.SmilesGenerator;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;

/**
 * The class that does the work.
 * @author nina
 *
 */
public class ChemWizard {
	private final static Logger LOGGER = Logger.getLogger(ChemWizard.class.getName());
	protected File file;
	public ChemWizard() {
		LOGGER.setLevel(Level.FINEST);
	}
	/**
	 * 
	 * @return
	 */
	public File getFile() {
		return file;
	}
	/**
	 * 
	 * @param file
	 */
	public void setFile(File file) {
		this.file = file;
	}
	/**
	 * 
	 * @param option
	 * @param argument
	 * @throws Exception
	 */
	public void setOption(_option option, String argument) throws Exception {
		if (argument!=null) argument = argument.trim();
		switch (option) {
		case file: {
			if ((argument==null) || "".equals(argument.trim())) return;
			setFile(new File(argument));
		}
		default: 
		}
	}
	private enum FIELD {
		XLogP,
		ALogP,
		ALogp2,
		AMR,
		SMILES_Kekule,
		SMILES_Aromatic
	}
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public int process() throws Exception {
		if (file==null) throw new Exception("File not assigned! Use -f command line option.");
		if (!file.exists()) throw new FileNotFoundException(file.getAbsolutePath());
		int records_read = 0;
		int records_processed = 0;
		int records_error = 0;
		
		InputStream in = new FileInputStream(file);
		/**
		 * cdk-io module
		 * http://ambit.uni-plovdiv.bg:8083/nexus/index.html#nexus-search;classname~IteratingMDLReader
		 */
		IteratingMDLReader reader = null;
		
		Writer writer = new BufferedWriter(new OutputStreamWriter(System.out));
		for (FIELD field : FIELD.values()) { 
			writer.write(field.name());
			writer.write(",");
		}
		writer.write("\n");
		try {
			/**
			 * cdk-slient module
			 * http://ambit.uni-plovdiv.bg:8083/nexus/index.html#nexus-search;classname~SilentChemObjectBuilder
			 */			
			reader = new IteratingMDLReader(in, DefaultChemObjectBuilder.getInstance());
			LOGGER.log(Level.INFO, String.format("Reading %s",file.getAbsoluteFile()));
			while (reader.hasNext()) {
				/**
				 * Note recent versions allow 
				 * IAtomContainer molecule  = reader.next();
				 */
				Object object = reader.next();
				IMolecule molecule = null;
				if (object instanceof IMolecule)
					molecule = (IMolecule)object;
				else break;
				
				records_read++;
				try {
					/**
					 * cdk-standard module
					 */
					AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(molecule);
					CDKHueckelAromaticityDetector.detectAromaticity(molecule);

					/**
					 * Generate SMILES and assign as properties
					 */
					assignSMILES(molecule);
					
					/**
					 * Descriptor calculation
					 */
					calculateLogP(molecule);
					
					/**
					 * Write as plain comma delimited 
					 */
					for (FIELD field : FIELD.values()) { 
						Object value = molecule.getProperty(field.name());
						writer.write(value==null?"":value.toString());
						writer.write(",");
					}
					writer.write("\n");
					records_processed++;;
				} catch (Exception x) {
					System.err.println("*");
					records_error++;
					LOGGER.log(Level.SEVERE, String.format("[Record %d] Error %s\n", records_read, file.getAbsoluteFile()), x);
				}

			}
		} catch (Exception x) {
			x.printStackTrace();
			LOGGER.log(Level.SEVERE, String.format("[Record %d] Error %s\n", records_read, file.getAbsoluteFile()), x);
		} finally {
			try { reader.close(); } catch (Exception x) {}
			try { writer.close(); } catch (Exception x) {}
		}
		LOGGER.log(Level.INFO, String.format("[Records read/processed/error %d/%d/%d] %s", 
						records_read,records_processed,records_error,file.getAbsoluteFile()));
		return records_read;
	}
	
	/**
	 * cdk-smiles module 
	 * http://ambit.uni-plovdiv.bg:8083/nexus/index.html#nexus-search;classname~SmilesGenerator
	 */
	private SmilesGenerator smilesGenerator = new SmilesGenerator();
	
	private XLogPDescriptor xlogp;
	private ALOGPDescriptor alogp;
	
	protected void assignSMILES(IMolecule molecule) throws Exception {
		smilesGenerator.setUseAromaticityFlag(false);
		molecule.setProperty(FIELD.SMILES_Kekule.name(),smilesGenerator.createSMILES(molecule));
		smilesGenerator.setUseAromaticityFlag(true);
		smilesGenerator.createSMILES(molecule);
		molecule.setProperty(FIELD.SMILES_Aromatic.name(),smilesGenerator.createSMILES(molecule));
		
	}
	
	protected void calculateLogP(IAtomContainer molecule) throws Exception {
		if (xlogp==null) xlogp = new XLogPDescriptor();
		if (alogp==null) alogp = new ALOGPDescriptor();
		
		DescriptorValue value = xlogp.calculate(molecule);
		String[] names = value.getNames();
		for (String name:names)
			molecule.setProperty(name, value.getValue().toString());
		
		value = alogp.calculate(molecule);
		names = value.getNames();
		if (value.getValue() instanceof DoubleArrayResult)
			for (int i=0; i < names.length;i++)
				molecule.setProperty(names[i], ((DoubleArrayResult)value.getValue()).get(i));
		
	}	
}
