
case class RandomWithState(seed: Long) {
  def nextInt: (Int, RandomWithState) = {
    val newSeed = (seed * 0x5DEECE66DL + 0xBL) & 0xFFFFFFFFFFFFL
    val nextRandom = RandomWithState(newSeed)
    val n = (newSeed >>> 16).toInt
    (n, nextRandom)
  }

  def nextIntRange(top: Int): (Int, RandomWithState) = {
    val newSeed = (seed * 0x5DEECE66DL + 0xBL) & 0xFFFFFFFFFFFFL
    val nextRandom = RandomWithState(newSeed)
    val n = (newSeed >>> 16).toInt % top
    (if (n < 0) -n else n, nextRandom)
  }
}