package net.idea.examples.cdk.maven_single_module;

import org.junit.Test;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import org.openscience.cdk.smiles.SmilesGenerator;
import org.openscience.cdk.smiles.SmilesParser;

public class SmilesTest {
	@Test
	public void test() throws Exception {
		String smiles =
			"CC(C)(C)C1=CC(=C(OP2OCC3(COP(OC4=CC=C(C=C4C(C)(C)C)C(C)(C)C)OC3)CO2)C=C1)C(C)(C)C";
			SmilesParser sp = new SmilesParser(SilentChemObjectBuilder.getInstance());
			IAtomContainer m = sp.parseSmiles(smiles);
			SmilesGenerator sg = SmilesGenerator.isomeric().aromatic();
			String s_out = sg.create(m);
			System.out.println(s_out);
	}
}
