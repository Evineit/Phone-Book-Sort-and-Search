package phonebook;

public class Duration {
    private long milliseconds;
    private long startTime;
    private long endTime;

    public Duration() {
    }

    public Duration(long milliseconds) {
        this.milliseconds = milliseconds;
    }

    public void start(){
        setStartTime(System.currentTimeMillis());
    }
    public void stop(){
        setEndTime(System.currentTimeMillis());
        if (getStartTime()>0){
            setMilliseconds(getEndTime()-getStartTime());
        }
    }
    public long getMillisSinceStart(){
        return (System.currentTimeMillis()-getStartTime());
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getMilliseconds() {
        return milliseconds;
    }

    public void setMilliseconds(long milliseconds) {
        this.milliseconds = milliseconds;
    }

    @Override
    public String toString() {
        return miliProcess(getMilliseconds());
    }

    /**
     * Gives a minutes, seconds and milliseconds format to the passed milliseconds
     * @param ms milliseconds to be processed
     * @return String of minutes, seconds and milliseconds.
     */
    public static String miliProcess(long ms) {
        long sec = ms / 1000;
        long mili = ms % 1000;
        long min = sec / 60;
        sec = sec % 60;
        return String.format("%s min. %s sec. %s ms.", min, sec, mili);
    }
}
