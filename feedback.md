# Feedback

The idea behind your game is great, and you've come a long way to make it work. However, I think you should work a bit more on keeping your code clean and reinforcing encapsulation. I left here some points on where you can improve your game and make the source code more readable, scalable and easier to maintain.

P.s: Since your main didn't have the source code, I reviewed the branch `JG-12` as pointed out in class by Henrique as being the main one.

#### The package `Grid`:

* Packages should always be named in lower case. It's a convention and it should be followed.

#### On the `BasePlayer` class:

* Avoid declaring two variables in the same line, as you did on lines 13 and 14. Even though possible, it's very frowned upon and worsens the readability.

* Revisit the method `checkPlayerCollision()` and try to break it into smaller methods to improve readability and maintainability.

#### On the `BotPlayer` class:

* Revisit the method `update()`, it is very big and hard to read. This can cause headaches when maintaining your source code, specially when it increases in size due to new features.

#### On the `Pallets` class:

* The code of this class, is confusing. The structure of it, should be revisited. Try placing all the properties on the top part of the class and the methods afterwards. The first method should be the constructor followed by the `addPallet()` and by the getter.

#### On the `Grid` class:

* Have you thought about a grid made by a two dimensional array (like `Rectangle[][] grid`)? It could give you coordinates of cols and rows a bit better, and it could be constructed using a nested loop. Try checking this option out!

#### On the `MouseController` class:

* Why do you felt the need to add the `volatile` keyword to the properties in this class? Do you know what it does?

#### On your `Game` class:

* Your `main()` method has a lot of the logic of your game. Try creating more methods on this class, and using encapsulation to help you create a more scalable and easy to maintain source code.

#### On your `Main` class:

* Which `main()` method should be run? Right now, you have two and even though some projects can have two mains, they usually represent two separate programs.



