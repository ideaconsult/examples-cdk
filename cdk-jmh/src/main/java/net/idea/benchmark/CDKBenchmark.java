package net.idea.benchmark;

import org.openjdk.jmh.annotations.Benchmark;
import org.openscience.cdk.config.AtomTypeFactory;
import org.openscience.cdk.exception.NoSuchAtomTypeException;
import org.openscience.cdk.interfaces.IAtomType;
import org.openscience.cdk.silent.SilentChemObjectBuilder;

public class CDKBenchmark {
	static AtomTypeFactory factory = AtomTypeFactory.getInstance("org/openscience/cdk/dict/data/cdk-atom-types.owl", SilentChemObjectBuilder.getInstance());
	
    @Benchmark
    public void testGetAtomType() throws NoSuchAtomTypeException {

    	IAtomType[] atomtypes = factory.getAllAtomTypes();
    	for (IAtomType atomtype: atomtypes) {
    		IAtomType type = factory.getAtomType(atomtype.getAtomTypeName());
    	}

    }

}
