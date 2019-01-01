class StopWatch {
    private long startTime;

    void start() {
        startTime = System.currentTimeMillis();
    }

    String stop() {
        String output = "";
        long elapsedTime = (System.currentTimeMillis() - startTime) / 1000;
        if (elapsedTime / 3600 > 0) output += (elapsedTime / 3600 + "時間");
        if (elapsedTime / 60 > 0) output += (elapsedTime / 60 - (elapsedTime / 3600) * 60 + "分");
        output += elapsedTime % 60 + "秒";
        return output;
    }
}