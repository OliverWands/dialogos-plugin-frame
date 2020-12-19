# dialogos-plugin-frame

A plugin for automatic generation of a dialog graph based on a frame.

## Examples

### Food order
An example frame that processes a simple food order consisting of a main dish, side dish,
a drink and whether or not the order is to go.\
- [foodOrderFrame.xml](examples/foodOrder/foodOrderFrame.xml) contains the exported frame and can be imported using a frame nodes import
functionality.
- [foodOrderGrammars.xml](examples/foodOrder/foodOrderGrammars.xml) contains the grammars that are used to match the input to a slot.
The grammars can be loaded for a frame while creating a new one or editing an existing one.
This file is for easy setting of grammars, but is not a necessity. Grammars that have been added
to the main graph can also be used for matching.
- [foodOrderGraph.xml](examples/foodOrder/foodOrderGraph.xml) is a saved dialog graph that contains the frame node.