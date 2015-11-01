package net.idea.benchmark;

import org.openjdk.jmh.annotations.Benchmark;
import org.openscience.cdk.config.AtomTypeFactory;
import org.openscience.cdk.exception.NoSuchAtomTypeException;
import org.openscience.cdk.interfaces.IAtomType;
import org.openscience.cdk.silent.SilentChemObjectBuilder;

public class CDKBenchmark {
	static AtomTypeFactory factory = AtomTypeFactory.getInstance(
			"org/openscience/cdk/dict/data/cdk-atom-types.owl",
			SilentChemObjectBuilder.getInstance());

	@Benchmark
	public int testGetAtomType() throws NoSuchAtomTypeException {
		int count = 0;
		IAtomType[] atomtypes = factory.getAllAtomTypes();
		for (int i = 0; i < 1000; i++)
			for (IAtomType atomtype : atomtypes) {
				IAtomType type = factory
						.getAtomType(atomtype.getAtomTypeName());
				count++;
			}
		return count;
	}

}
