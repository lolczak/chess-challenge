# Scalac Chess Challenge

1. Run App

> sbt "run 7 7 K2 Q2 B2 N1"

2. Remarks

I had to use a huge stack size due to problems with the foldMap function of Free monad. It occurred that the function is not stack safe.