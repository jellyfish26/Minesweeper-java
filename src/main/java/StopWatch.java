class StopWatch {
  private long startTime;

  void start() {
    startTime = System.currentTimeMillis();
  }

  String genTimeString(String nameBase, long value) {
    if (value == 1) {
      return String.format(" %d %s", value, nameBase);
    } else if(value >= 2) {
      return String.format(" %d %ss", value, nameBase);
    }
    return "";
  }

  String stop() {
    StringBuilder ret = new StringBuilder();
    long elapsedTime = (System.currentTimeMillis() - startTime) / 1000;

    if (elapsedTime >= 3600) {
      ret.append(genTimeString("hour", elapsedTime / 3600));
    }

    if (elapsedTime >= 60) {
      long hours = (elapsedTime / 60) - (elapsedTime / 3600) * 60;
      ret.append(genTimeString("minute", hours));
    }

    long seconds = elapsedTime % 60;
    ret.append(genTimeString("second", seconds));
    return ret.toString();
  }
}
