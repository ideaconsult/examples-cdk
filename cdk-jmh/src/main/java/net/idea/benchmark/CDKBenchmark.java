package net.idea.benchmark;

import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Warmup;
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
@Warmup(iterations = 5)
@Measurement(iterations = 5)
@Fork(1)
@State(Scope.Benchmark)
public class CDKBenchmark {

	@State(Scope.Benchmark)
	public static class BenchmarkFactory {
		AtomTypeFactory instance;

		@Setup(Level.Trial)
		public void initialize() {
			instance = AtomTypeFactory.getInstance(
					"org/openscience/cdk/dict/data/cdk-atom-types.owl",
					SilentChemObjectBuilder.getInstance());
		}

		@TearDown(Level.Trial)
		public void shutdown() {
			// Nothing to do
		}
	}

	@Benchmark
	@OutputTimeUnit(TimeUnit.NANOSECONDS)
	public int testGetAtomType(BenchmarkFactory factory) throws NoSuchAtomTypeException {
		int count = 0;
		IAtomType[] atomtypes = factory.instance.getAllAtomTypes();
		for (IAtomType atomtype : atomtypes) {
			IAtomType type = factory.instance.getAtomType(atomtype.getAtomTypeName());
			count++;
		}
		return count;
	}

	/*
	 * @Benchmark
	 * 
	 * @OutputTimeUnit(TimeUnit.NANOSECONDS) public int testGetAtomTypeLoop()
	 * throws NoSuchAtomTypeException { int count = 0; IAtomType[] atomtypes =
	 * factory.getAllAtomTypes(); for (int i = 0; i < 1000; i++) for (IAtomType
	 * atomtype : atomtypes) { IAtomType type = factory
	 * .getAtomType(atomtype.getAtomTypeName()); count++; } return count; }
	 */
	public static void main(String[] args) throws RunnerException {
		Options opt = new OptionsBuilder()
				.include(".*" + CDKBenchmark.class.getSimpleName() + ".*")
				.warmupIterations(5).measurementIterations(5).forks(1).build();

		new Runner(opt).run();
	}
}
