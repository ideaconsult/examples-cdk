package net.idea.examples.cdk.maven_single_module;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.openscience.cdk.inchi.InChIGenerator;
import org.openscience.cdk.inchi.InChIGeneratorFactory;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond.Order;
import org.openscience.cdk.silent.Atom;
import org.openscience.cdk.silent.AtomContainer;
import org.openscience.cdk.silent.Bond;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import org.openscience.cdk.smiles.SmilesParser;
import org.openscience.cdk.tools.CDKHydrogenAdder;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;

import junit.framework.Assert;
import net.sf.jniinchi.INCHI_OPTION;
import net.sf.jniinchi.INCHI_RET;

public class InchiTest {

	protected static InChIGeneratorFactory factory;

	protected InChIGeneratorFactory getFactory() throws Exception {
		if (factory == null) {
			factory = InChIGeneratorFactory.getInstance();
		}
		return (factory);
	}

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
		AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(mol1);
		CDKHydrogenAdder.getInstance(mol1.getBuilder()).addImplicitHydrogens(mol1);

		IAtomContainer mol2 = p.parseSmiles("SN=NCC");
		AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(mol2);
		CDKHydrogenAdder.getInstance(mol2.getBuilder()).addImplicitHydrogens(mol2);

		InChIGenerator gen1 = getFactory().getInChIGenerator(mol1, options);
		System.out.println(gen1.getReturnStatus());
		System.out.println(gen1.getMessage());
		// Assert.assertEquals(INCHI_RET.OKAY, gen1.getReturnStatus());

		InChIGenerator gen2 = getFactory().getInChIGenerator(mol2, options);
		// Assert.assertEquals(INCHI_RET.OKAY, gen2.getReturnStatus());
		System.out.println(gen2.getReturnStatus());
		System.out.println(gen2.getMessage());

		Assert.assertEquals("InChI=1/C2H6N2S/c1-2-3-4-5/h2H2,1H3,(H,3,5)/f/h3H", gen1.getInchi());
		Assert.assertEquals("HLBPZSLHBUKVDX-TULZNQERNA-N", gen1.getInchiKey());
		Assert.assertEquals("InChI=1/C2H6N2S/c1-2-3-4-5/h2H2,1H3,(H,3,5)/f/h5H", gen2.getInchi());
		Assert.assertEquals("HLBPZSLHBUKVDX-JSWHHWTPNA-N", gen2.getInchiKey());
		Assert.assertTrue(!gen1.getInchi().equals(gen2.getInchi()));
		Assert.assertTrue(!gen1.getInchiKey().equals(gen2.getInchiKey()));
	}

	@Test
	public void nonStandardInChIWithEnumOptions() throws Exception {
		IAtomContainer ac = new AtomContainer();
		IAtom a1 = new Atom("C");
		IAtom a2 = new Atom("C");
		a1.setImplicitHydrogenCount(3);
		a2.setImplicitHydrogenCount(3);
		ac.addAtom(a1);
		ac.addAtom(a2);
		ac.addBond(new Bond(a1, a2, Order.SINGLE));
		List<INCHI_OPTION> options = new ArrayList<INCHI_OPTION>();
		options.add(INCHI_OPTION.FixedH);
		options.add(INCHI_OPTION.SAbs);
		options.add(INCHI_OPTION.SAsXYZ);
		options.add(INCHI_OPTION.SPXYZ);
		options.add(INCHI_OPTION.FixSp3Bug);
		options.add(INCHI_OPTION.AuxNone);
		InChIGenerator gen = getFactory().getInChIGenerator(ac, options);
		Assert.assertEquals(gen.getReturnStatus(), INCHI_RET.OKAY);
		Assert.assertEquals("InChI=1/C2H6/c1-2/h1-2H3", gen.getInchi());
		Assert.assertEquals("OTMSDBZUPAUEDD-UHFFFAOYNA-N", gen.getInchiKey());
	}
}
