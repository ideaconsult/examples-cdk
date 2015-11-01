package net.idea.benchmark;

import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openscience.cdk.config.AtomTypeFactory;
import org.openscience.cdk.exception.NoSuchAtomTypeException;
import org.openscience.cdk.interfaces.IAtomType;
import org.openscience.cdk.silent.SilentChemObjectBuilder;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class CDKBenchmark {
	final static AtomTypeFactory factory=AtomTypeFactory.getInstance(
				"org/openscience/cdk/dict/data/cdk-atom-types.owl",
				SilentChemObjectBuilder.getInstance());
	

	@Benchmark
	@OutputTimeUnit(TimeUnit.NANOSECONDS)
	public int testGetAtomType() throws NoSuchAtomTypeException {
		int count = 0;
		IAtomType[] atomtypes = factory.getAllAtomTypes();
		for (IAtomType atomtype : atomtypes) {
			IAtomType type = factory.getAtomType(atomtype.getAtomTypeName());
			count++;
		}
		return count;
	}

	@Benchmark
	@OutputTimeUnit(TimeUnit.NANOSECONDS)
	public int testGetAtomTypeLoop() throws NoSuchAtomTypeException {
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

	public static void main(String[] args) throws RunnerException {
		Options opt = new OptionsBuilder()
				.include(".*" + CDKBenchmark.class.getSimpleName() + ".*")
				.warmupIterations(5).measurementIterations(5).forks(1).build();

		new Runner(opt).run();
	}
}
