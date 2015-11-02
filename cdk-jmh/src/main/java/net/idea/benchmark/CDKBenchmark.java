package net.idea.benchmark;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
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
import org.openscience.cdk.atomtype.CDKAtomTypeMatcher;
import org.openscience.cdk.config.AtomTypeFactory;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.exception.NoSuchAtomTypeException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomType;
import org.openscience.cdk.io.MDLReader;
import org.openscience.cdk.silent.AtomContainer;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import org.openscience.cdk.tools.manipulator.AtomTypeManipulator;

import com.google.common.base.Verify;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 5)
@Measurement(iterations = 10)
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

		}
	}

	@State(Scope.Benchmark)
	public static class Molecule {
		IAtomContainer instance = null;

		@Setup(Level.Trial)
		public void initialize() {
			MDLReader reader = null;
			try {
				IAtomContainer mol = null;
				InputStream in = getClass().getClassLoader()
						.getResourceAsStream(
								"net/idea/benchmark/CHEMBL2373570.sdf");
				Verify.verifyNotNull(in);
				reader = new MDLReader(in);
				mol = reader.read(new AtomContainer());
				instance = mol;
				Verify.verifyNotNull(instance);
				Verify.verify(instance.getAtomCount() == 581);
			} catch (CDKException x) {
				x.printStackTrace();

			} finally {
				try {
					if (reader != null)
						reader.close();
				} catch (Exception x) {
				}
			}

		}
	}

	@Benchmark
	@OutputTimeUnit(TimeUnit.NANOSECONDS)
	public int testGetAtomType(BenchmarkFactory factory)
			throws NoSuchAtomTypeException {
		int count = 0;
		IAtomType[] atomtypes = factory.instance.getAllAtomTypes();
		for (IAtomType atomtype : atomtypes) {
			IAtomType type = factory.instance.getAtomType(atomtype
					.getAtomTypeName());
			Assert.assertNotNull(type);
			count++;
		}
		Assert.assertTrue(count > 0);
		return count;
	}

	@Benchmark
	@OutputTimeUnit(TimeUnit.NANOSECONDS)
	public IAtomContainer testPerceiveAtomType(Molecule molecule)
			throws CDKException {
		CDKAtomTypeMatcher matcher = CDKAtomTypeMatcher
				.getInstance(SilentChemObjectBuilder.getInstance());
		for (IAtom atom : molecule.instance.atoms()) {
			IAtomType matched = matcher.findMatchingAtomType(molecule.instance,
					atom);
			if (matched != null)
				AtomTypeManipulator.configure(atom, matched);
		}
		return molecule.instance;
	}

	public static void main(String[] args) throws RunnerException {
		Options opt = new OptionsBuilder()
				.include(".*" + CDKBenchmark.class.getSimpleName() + ".*")
				.warmupIterations(5).measurementIterations(5).forks(1).build();

		new Runner(opt).run();
	}
}
