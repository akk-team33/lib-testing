package de.team33.testing.async.thebe;

/**
 * Summarizes various input parameters that may be of interest in the context of the execution of an
 * {@link de.team33.patterns.exceptional.dione.XFunction Operation}.
 *
 * @see Parallel#report(int, de.team33.patterns.exceptional.dione.XFunction)
 * @see Parallel#stream(int, de.team33.patterns.exceptional.dione.XFunction)
 */
public class Context {

  /**
   * The index of the executing thread in the order of its instantiation.
   */
  public final int threadIndex;

  /**
   * The index of the operation in the order of the actual beginning of its execution.
   */
  public final int operationIndex;

  /**
   * The index of the operation loop within one execution;
   */
  public final int loopIndex;

  public Context(final int threadIndex, final int operationIndex, final int loopIndex) {
    this.threadIndex = threadIndex;
    this.operationIndex = operationIndex;
    this.loopIndex = loopIndex;
  }
}
