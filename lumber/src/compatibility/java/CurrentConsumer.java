import br.com.arch.toolkit.lumber.Lumber;
import java.util.ArrayList;
import java.util.List;

/** Protects the 1.2+ facade descriptors while restoring the older Oak contract. */
public final class CurrentConsumer {
    private static final class Sink extends Lumber.Oak {
        final List<String> entries = new ArrayList<>();
        @Override public boolean isLoggable(String tag, Lumber.Level level) { return true; }
        @Override protected void log(Lumber.Level level, String tag, String message, Throwable error) {
            entries.add(tag + ":" + message);
        }
    }

    public static void main(String[] args) {
        Sink sink = new Sink();
        Lumber.OakWood.uprootAll();
        Lumber.OakWood.plant(sink);
        Lumber.TaggedLumber logger = Lumber.OakWood.tag("Facade");
        logger.quiet(true).debug("hidden");
        logger.maxLogLength(3).maxTagLength(2).debug("abc");
        logger.debug("retained");
        Lumber.OakWood.quiet(true).maxLogLength(3).maxTagLength(2).debug("hidden");
        if (sink.entries.size() != 2 || !sink.entries.get(0).equals("Fa:abc")
                || !sink.entries.get(1).equals("Facade:retained")) {
            throw new AssertionError(sink.entries.toString());
        }
        Lumber.OakWood.uprootAll();
        System.out.println("Current released consumer passed against candidate publication.");
    }
}
