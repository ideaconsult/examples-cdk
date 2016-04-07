package net.idea.examples.cdk.maven_single_module;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import junit.framework.Assert;

import org.junit.Test;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.io.MDLV2000Reader;
import org.openscience.cdk.silent.AtomContainer;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import org.openscience.cdk.smiles.SmilesGenerator;
import org.openscience.cdk.smiles.SmilesParser;

public class SmilesTest {
	@Test
	public void test() throws Exception {
		String smiles = "CC(C)(C)C1=CC(=C(OP2OCC3(COP(OC4=CC=C(C=C4C(C)(C)C)C(C)(C)C)OC3)CO2)C=C1)C(C)(C)C";
		SmilesParser sp = new SmilesParser(
				SilentChemObjectBuilder.getInstance());
		IAtomContainer m = sp.parseSmiles(smiles);
		SmilesGenerator sg = SmilesGenerator.absolute();
		Assert.assertEquals(
				"CC(C)(C)C1=CC=C(C(=C1)C(C)(C)C)OP2OCC3(CO2)COP(OC3)OC4=CC=C(C=C4C(C)(C)C)C(C)(C)C",
				sg.create(m));
	}

	@Test
	public void weirdtest() throws Exception {
		String smiles = null;
		InputStream in = getClass().getClassLoader().getResourceAsStream("test/CHEMBL1204342.mol");
		Assert.assertNotNull(in);
		MDLV2000Reader reader = null;

		try {
			reader = new MDLV2000Reader(in);
			IAtomContainer mol = reader.read(new AtomContainer());
			SmilesGenerator g = SmilesGenerator.isomeric();
			smiles = g.create(mol);
		} finally {
			if (reader!=null) reader.close();
		}
		//final String smiles = "CC(C)NC(C=1C=CC=2C(C1)=N\\C(=C/3\\C=C/C(/C=C3)=C\\4/C=C/C(=C/5\\C=C/C(/C=C5)=C/6\\N=C7C=CC(=CC7=N6)C(=N)NC(C)C)/O4)\\N2)=N";
		weirdtest_smiles(smiles);
	}
	public void weirdtest_smiles(String smiles) throws Exception {

		SmilesGenerator smiles_generator = SmilesGenerator.isomeric();
		SmilesParser sp = new SmilesParser(
				SilentChemObjectBuilder.getInstance());
		Map<String, Integer> map = new HashMap<String, Integer>();
		int failure = 0;
		for (int i = 0; i < 100; i++)
			try {
				IAtomContainer mol = sp.parseSmiles(smiles);
				String newsmiles = smiles_generator.create(mol);
				// System.out.println(newsmiles);
				Integer n = map.get(newsmiles);
				if (n == null)
					map.put(newsmiles, 1);
				else
					map.put(newsmiles, n + 1);

			} catch (Exception x) {
				failure++;
				x.printStackTrace();
			}

		System.out.println(String.format("Starting SMILES\n%s\nGenerated %s different SMILES;\t%s failures", smiles,map.size(),failure));

		Iterator<Entry<String, Integer>> t = map.entrySet().iterator();
		while (t.hasNext()) {
			Entry<String, Integer> entry = t.next();
			System.out.println(String.format("%s\t%s", entry.getValue(),
					entry.getKey()));
			//sp.parseSmiles(entry.getKey());
		}

		Assert.assertEquals(0, failure);
	}
}
