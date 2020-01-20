/**
  * Created by patry on 09.11.2017.
  */
object MainObject {

  def main(args: Array[String]): Unit = {
    println("Welcome in main class")
    println(speed(100, 20))
    printTime(Console.err)

    val numbers = List(2,3,4,5)

    println(my_map(numbers, addone))
    println(numbers.map(addone))

    val anotherNumbers = List(-10, -5, -4, -3, 12, 23, 34)
    println(anotherNumbers.map((x:Int) => x+1))

    println(gradientDescent(funDeriv, 1, 0.00001))
  }

  def addone(n: Int) = n+1

  def speed(distance: Double, time: Double) : Double = {
    distance/time
  }

  def printTime(out: java.io.PrintStream = Console.out) ={
    out.println("time = " + System.currentTimeMillis())
  }

  def my_map(lst: List[Int], fun: Int => Int) : List[Int] = {
    for(l <- lst) yield fun(l)
  }

  def funDeriv(x : Double) = 4 * math.pow(x, 3) - 9 * math.pow(x, 2)
  def gradientDescent(f : Double => Double,
                      init: Double,
                      rate : Double) = {
    def rec(next : Double, old: Double) : Double = {
      if(math.abs(next - old) <= 0.00001) next
      else rec(next - rate * f(next), next)
    }


  }

}
