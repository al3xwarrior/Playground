# 🚧 Playground Action Rework
This fork aims to rework the action system to be more robust and flexible. It is completely unfinished.

### Todo
- [ ] Add grouping action properties?
- [ ] Work more on the lore of each action and how it's properties are previewed. In some cases too many properties will be shown.
- [ ] Work on a single toData and fromData method that can be used for all actions. This will make it easier to add new actions and properties.
- [ ] Separate static buttons from being ActionProperties. They don't hold values, so they shouldn't be defined as properties. Maybe those can be seperated into a separate system that allows custom gui buttons for each action. See FunctionAction and NPCStatAction for examples.