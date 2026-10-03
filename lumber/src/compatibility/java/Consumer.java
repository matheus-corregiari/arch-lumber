import br.com.arch.toolkit.lumber.Lumber;
import java.util.ArrayList;
import java.util.List;

/** Bytecode compiled against a released API, never recompiled against the candidate. */
public final class Consumer {
    private static final class Sink extends Lumber.Oak {
        final List<String> entries = new ArrayList<>();

        @Override public boolean isLoggable(String tag, Lumber.Level level) { return true; }
        @Override protected void log(Lumber.Level level, String tag, String message, Throwable error) {
            entries.add(tag + ":" + message);
        }
    }

    private static final class AccessorSink extends Lumber.Oak {
        @Override protected String getTag() { return super.getTag(); }
        @Override protected boolean getQuiet() { return super.getQuiet(); }
        @Override protected Integer getMaxLogLength() { return super.getMaxLogLength(); }
        @Override protected Integer getMaxTagLength() { return super.getMaxTagLength(); }
        @Override public boolean isLoggable(String tag, Lumber.Level level) { return true; }
        @Override protected void log(Lumber.Level level, String tag, String message, Throwable error) { }
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }

    public static void main(String[] args) {
        Sink sink = new Sink();
        Lumber.OakWood.uprootAll();
        Lumber.OakWood.plant(sink);
        // Same invocation contract found in EasyNavigation 1.1.0 NavigationController bytecode.
        Lumber.Oak logger = Lumber.OakWood.tag("NavigationController");
        logger.error("Cannot pop root destination.");
        require(sink.entries.get(0).equals("NavigationController:Cannot pop root destination."), "tag/error");
        logger.error(new IllegalArgumentException("payload"), "Failed decoding payload.");
        require(sink.entries.get(1).startsWith("NavigationController:Failed decoding payload."), "tag/throwable");
        int count = sink.entries.size();
        Lumber.OakWood.quiet(true).debug("hidden");
        require(sink.entries.size() == count, "quiet");
        Lumber.OakWood.maxLogLength(3).tag("Chunks").debug("abcdef");
        require(sink.entries.get(count).equals("Chunks #0:abc"), "maxLogLength");
        Lumber.OakWood.maxTagLength(3).tag("LongTag").debug("message");
        require(sink.entries.get(sink.entries.size() - 1).equals("Lon:message"), "maxTagLength");
        // Invoke the methods on an Oak reference as older Kotlin consumers do.
        Lumber.Oak oak = Lumber.OakWood;
        oak.tag("Oak").quiet(true).debug("hidden");
        oak.tag("Oak").maxLogLength(3).maxTagLength(2).debug("abc");
        require(sink.entries.get(sink.entries.size() - 1).equals("Oa:abc"), "Oak chain");
        Lumber.OakWood.uprootAll();
        AccessorSink accessors = new AccessorSink();
        require(accessors.getTag() == null, "protected tag/super getter");
        Lumber.Oak direct = accessors.tag(" Legacy ");
        direct.debug("fixed tag facade");
        accessors.quiet(true).maxLogLength(7).maxTagLength(5);
        require(accessors.getQuiet(), "protected quiet/super getter");
        require(!accessors.getQuiet(), "quiet consumed");
        require(Integer.valueOf(7).equals(accessors.getMaxLogLength()), "protected maxLogLength");
        require(accessors.getMaxLogLength() == null, "maxLogLength consumed");
        require(Integer.valueOf(5).equals(accessors.getMaxTagLength()), "protected maxTagLength");
        require(accessors.getMaxTagLength() == null, "maxTagLength consumed");
        System.out.println("Released consumer passed against candidate publication.");
    }
}
