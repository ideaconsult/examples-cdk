package net.idea.examples.cdk.maven_single_module;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.openscience.cdk.inchi.InChIGenerator;
import org.openscience.cdk.inchi.InChIGeneratorFactory;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import org.openscience.cdk.smiles.SmilesParser;

import junit.framework.Assert;
import net.sf.jniinchi.INCHI_OPTION;

public class InchiTest {

	@Test
	public void testTautomers() throws Exception {

		List<INCHI_OPTION> options = new ArrayList<INCHI_OPTION>();
		options.add(INCHI_OPTION.FixedH);
		options.add(INCHI_OPTION.SAbs);
		options.add(INCHI_OPTION.SAsXYZ);
		options.add(INCHI_OPTION.SPXYZ);
		options.add(INCHI_OPTION.FixSp3Bug);
		options.add(INCHI_OPTION.AuxNone);

		SmilesParser p = new SmilesParser(SilentChemObjectBuilder.getInstance());
		IAtomContainer mol1 = p.parseSmiles("S=NNCC");
		//AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(mol1);
		//CDKHydrogenAdder.getInstance(mol1.getBuilder()).addImplicitHydrogens(mol1);

		IAtomContainer mol2 = p.parseSmiles("SN=NCC");
		//AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(mol2);
		//CDKHydrogenAdder.getInstance(mol2.getBuilder()).addImplicitHydrogens(mol2);

		InChIGeneratorFactory factory = InChIGeneratorFactory.getInstance();

		InChIGenerator gen1 = factory.getInChIGenerator(mol1, options);
		System.out.println(gen1.getReturnStatus());
		System.out.println(gen1.getMessage());
		// Assert.assertEquals(INCHI_RET.OKAY, gen1.getReturnStatus());

		InChIGenerator gen2 = factory.getInChIGenerator(mol2, options);
		// Assert.assertEquals(INCHI_RET.OKAY, gen2.getReturnStatus());
		System.out.println(gen2.getReturnStatus());
		System.out.println(gen2.getMessage());

		Assert.assertEquals("InChI=1/C2H6N2S/c1-2-3-4-5/h2H2,1H3,(H,3,5)/f/h3H", gen1.getInchi());
		Assert.assertEquals("HLBPZSLHBUKVDX-TULZNQERNA-N",gen1.getInchiKey());
		Assert.assertEquals("InChI=1/C2H6N2S/c1-2-3-4-5/h2H2,1H3,(H,3,5)/f/h5H",gen2.getInchi());
		Assert.assertEquals("HLBPZSLHBUKVDX-JSWHHWTPNA-N", gen2.getInchiKey());
		Assert.assertTrue(!gen1.getInchi().equals(gen2.getInchi()));
		Assert.assertTrue(!gen1.getInchiKey().equals(gen2.getInchiKey()));
	}
}
