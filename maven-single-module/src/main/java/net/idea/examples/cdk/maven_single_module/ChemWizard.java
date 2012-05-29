package net.idea.examples.cdk.maven_single_module;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.idea.examples.cdk.maven_single_module.MainApp._option;

import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.aromaticity.CDKHueckelAromaticityDetector;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.io.SDFWriter;
import org.openscience.cdk.io.iterator.IteratingMDLReader;
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
		SDFWriter writer = new SDFWriter(System.out);
		try {
			/**
			 * cdk-slient module
			 * http://ambit.uni-plovdiv.bg:8083/nexus/index.html#nexus-search;classname~SilentChemObjectBuilder
			 */			
			reader = new IteratingMDLReader(in, DefaultChemObjectBuilder.getInstance());
			LOGGER.log(Level.INFO, String.format("Reading %s",file.getAbsoluteFile()));
			while (reader.hasNext()) {
				IAtomContainer molecule = reader.next();
				records_read++;
				try {
					/**
					 * cdk-standard module
					 */
					AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(molecule);
					CDKHueckelAromaticityDetector.detectAromaticity(molecule);
					
					/**
					 * The next statement will remove the properties, hence we keep a reference
					 */
					Map<Object,Object> properties = molecule.getProperties();
					/**
					 * cdk-valencycheck module 
					 * http://ambit.uni-plovdiv.bg:8083/nexus/index.html#nexus-search;classname~cdkhydrogenadder
					 */
					molecule = AtomContainerManipulator.removeHydrogensPreserveMultiplyBonded(molecule);
					/**
					 * Set the properties back
					 */
					molecule.setProperties(properties);
					
					/**
					 * Generate SMILES and assign as properties
					 */
					smilesGenerator.setUseAromaticityFlag(false);
					molecule.setProperty("SMILES (Kekule)",smilesGenerator.createSMILES(molecule));
					smilesGenerator.setUseAromaticityFlag(true);
					molecule.setProperty("SMILES (Aromatic)",smilesGenerator.createSMILES(molecule));
					
					writer.write(molecule);
					System.err.print(".");
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
	
	protected void assignSMILES(IAtomContainer molecule) throws Exception {
		smilesGenerator.setUseAromaticityFlag(false);
		molecule.setProperty("SMILES (Kekule)",smilesGenerator.createSMILES(molecule));
		smilesGenerator.setUseAromaticityFlag(true);
		smilesGenerator.createSMILES(molecule);
		molecule.setProperty("SMILES (Aromatic)",smilesGenerator.createSMILES(molecule));
		
	}
}
