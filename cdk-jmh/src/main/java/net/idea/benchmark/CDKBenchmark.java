package net.idea.benchmark;

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
import org.openscience.cdk.config.Isotopes;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.exception.NoSuchAtomTypeException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomType;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.silent.Atom;
import org.openscience.cdk.silent.AtomContainer;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import org.openscience.cdk.tools.manipulator.AtomTypeManipulator;

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
		IAtomContainer instance;

		@Setup(Level.Trial)
		public void initialize() {

			IAtomContainer mol = new AtomContainer();
			mol.addAtom(new Atom("C")); // 0
			mol.addAtom(new Atom("C")); // 1
			mol.addAtom(new Atom("C")); // 2
			mol.addAtom(new Atom("C")); // 3
			mol.addAtom(new Atom("C")); // 4
			mol.addAtom(new Atom("C")); // 5
			mol.addAtom(new Atom("C")); // 6
			mol.addAtom(new Atom("C")); // 7
			mol.addAtom(new Atom("C")); // 8
			mol.addAtom(new Atom("C")); // 9
			mol.addAtom(new Atom("C")); // 10
			mol.addAtom(new Atom("C")); // 11
			mol.addAtom(new Atom("C")); // 12
			mol.addAtom(new Atom("C")); // 13
			mol.addAtom(new Atom("C")); // 14
			mol.addAtom(new Atom("C")); // 15
			mol.addAtom(new Atom("C")); // 16
			mol.addAtom(new Atom("C")); // 17
			mol.addAtom(new Atom("C")); // 18

			mol.addBond(0, 1, IBond.Order.SINGLE); // 1
			mol.addBond(1, 2, IBond.Order.DOUBLE); // 2
			mol.addBond(2, 3, IBond.Order.SINGLE); // 3
			mol.addBond(3, 4, IBond.Order.DOUBLE); // 4
			mol.addBond(4, 5, IBond.Order.SINGLE); // 5
			mol.addBond(5, 6, IBond.Order.DOUBLE); // 6
			mol.addBond(6, 7, IBond.Order.SINGLE); // 8
			mol.addBond(7, 8, IBond.Order.DOUBLE); // 9
			mol.addBond(8, 9, IBond.Order.SINGLE); // 10
			mol.addBond(9, 0, IBond.Order.DOUBLE); // 11
			mol.addBond(9, 4, IBond.Order.SINGLE); // 12
			mol.addBond(8, 10, IBond.Order.SINGLE); // 12
			mol.addBond(10, 11, IBond.Order.DOUBLE); // 12
			mol.addBond(11, 12, IBond.Order.SINGLE); // 12
			mol.addBond(12, 13, IBond.Order.DOUBLE); // 12
			mol.addBond(13, 7, IBond.Order.SINGLE); // 12
			mol.addBond(3, 14, IBond.Order.SINGLE); // 12
			mol.addBond(14, 15, IBond.Order.SINGLE); // 12
			mol.addBond(12, 16, IBond.Order.SINGLE); // 12
			mol.addBond(16, 17, IBond.Order.SINGLE); // 12
			mol.addBond(17, 18, IBond.Order.SINGLE); // 12
			try {
				Isotopes.getInstance().configureAtoms(mol);
			} catch (Exception x) {
				x.printStackTrace();
			}
			instance = mol;
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
		Assert.assertTrue(count>0);
		return count;
	}
	

	@Benchmark
	@OutputTimeUnit(TimeUnit.NANOSECONDS)
	public IAtomContainer testPerceiveAtomType(Molecule molecule) throws CDKException {
	    CDKAtomTypeMatcher matcher = CDKAtomTypeMatcher.getInstance(molecule.instance.getBuilder());
        for (IAtom atom : molecule.instance.atoms()) {
            IAtomType matched = matcher.findMatchingAtomType(molecule.instance, atom);
            if (matched != null) AtomTypeManipulator.configure(atom, matched);
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
