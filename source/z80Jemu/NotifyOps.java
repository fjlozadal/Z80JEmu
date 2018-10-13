
package z80Jemu;

/**
 *
 * @author jsanchez
 */
public interface NotifyOps {
    int breakpoint(int address, int opcode);
    void execDone();
}
