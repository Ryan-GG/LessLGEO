<div align="center" style="text-align: center">
<h1>Less LGEO </h1>
<p>
  Find the minimum amount of Lego Bricks needed
</p>
<p>
  <a href="https://github.com/Ryan-GG/LessLGEO/actions/workflows/main.yml">
    <img alt="Main Yaml Status" src="https://github.com/Ryan-GG/LessLGEO/actions/workflows/main.yml/badge.svg">
  </a>
</p>
</div>

# Contents

- [Summary](#summary)
- [Quick-Start](#quick-start)

# Summary

LessLGEO is a web based Next.js, SpringBoot backend application designed to find the set of
**required** pieces for a given Lego set. Required pieces are defined as pieces that contribute to
the Model's appearance and/or supporting structure. LessLGEO is designed to solve the problem of
unnecessary(filler) pieces that are normally hidden to a viewer such as interior, hidden, or small
Lego parts that tend to be included in sets.

<div align="center" style="text-align: center">
    <img src="docs/resources/software_architecture_diagram.drawio.svg"/>
</div>

## Determining <i>Required Parts</i>

There are three main contributors to what affects the <i>need</i> of a Lego part in a set. For this
problem we can break it down into three attribute for any given part.

- Cost
- Visibility
- Structural Importance

### Cost

Most people buy their Lego sets at a store for a fixed price and the price tends to remain stagnant
besides a few sales until the Set is retired by Lego. Though for enthusiasts that like making
custom-builds it's easier to buy particular bricks rather than get parts through sets. There are
many websites that sell Lego parts such as [Bricklink](https://www.bricklink.com/v2/main.page),
[BrickOwl](https://www.brickowl.com/us/), and
even [Lego's Pick-A-Brick](https://www.lego.com/en-us/pick-and-build/pick-a-brick). LessLGEO aims
to support these enthusiasts by allowing them to only have to buy the pieces they need to build
their designs. As pieces range in price and if they originate from a now retired set it's surely the
case that the price for that piece has also increased. Higher the price the part, the less we want
to include it in the required set of parts.

### Visibility

Personally I have many display sets, by that I mean sets that are in the same position and just for
display like a piece of art. There is many pieces to the whole set that never even see light most of
the time. So why include those pieces, it's not like anyone's going to see them. LessLGEO takes this
into consideration, determining a pieces visibility based on input of light sources relative to a
Model. The less light bounces off of a part, the less its needed in a set to begin with.

### Structural Importance

While a Lego part might not be seen, it can't guarantee that the part isn't important to actually
building a Model. Lego parts only have other Lego parts to support themselves and create the
structure we view as a user. So we take into account the number of other parts an individual part
connects to know that maybe this part shouldn't be removed because its supporting other pieces.

# Quick-Start

 
