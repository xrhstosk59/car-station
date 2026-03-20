package car.station;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import javafx.application.Platform;

final class FxTestSupport {

    private static final AtomicBoolean STARTED = new AtomicBoolean(false);

    private FxTestSupport() {
    }

    static void initToolkit() throws InterruptedException {
        if (STARTED.compareAndSet(false, true)) {
            CountDownLatch latch = new CountDownLatch(1);
            Platform.startup(latch::countDown);
            if (!latch.await(10, TimeUnit.SECONDS)) {
                throw new IllegalStateException("Timed out starting JavaFX toolkit");
            }
        }
    }

    static void runOnFxThread(ThrowingRunnable action) throws Throwable {
        AtomicReference<Throwable> error = new AtomicReference<>();
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                action.run();
            } catch (Throwable throwable) {
                error.set(throwable);
            } finally {
                latch.countDown();
            }
        });

        if (!latch.await(10, TimeUnit.SECONDS)) {
            throw new IllegalStateException("Timed out waiting for JavaFX action");
        }
        if (error.get() != null) {
            throw error.get();
        }
    }

    @FunctionalInterface
    interface ThrowingRunnable {
        void run() throws Throwable;
    }
}
