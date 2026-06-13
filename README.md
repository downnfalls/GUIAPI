# 🧩 GUIAPI

A modular, component-based GUI framework for Spigot/Paper plugins. Build interactive inventory UIs with buttons, text inputs, scrollable lists, pagination, editable slots, and more — all through a clean, declarative API.

[![](https://jitpack.io/v/downnfalls/GUIAPI.svg)](https://jitpack.io/#downnfalls/GUIAPI)

---

## 📑 Table of Contents

- [Features](#-features)
- [Requirements](#-requirements)
- [Installation](#-installation)
- [Quick Start](#-quick-start)
- [Core Concepts](#-core-concepts)
  - [GUIAPI & GUILibs](#guiapi--guilibs)
  - [GUI](#gui)
  - [GuiRenderer](#guirenderer)
  - [GuiComponent (Base Class)](#guicomponent-base-class)
- [Components](#-components)
  - [GuiButton](#guibutton)
  - [GuiCheckButton](#guicheckbutton)
  - [GuiOptionButton](#guioptionbutton)
  - [GuiTextInput](#guitextinput)
  - [GuiListTextInput](#guilisttextinput)
  - [GuiItemChooser](#guiitemchooser)
  - [GuiEditableSlot](#guieditableslot)
  - [GuiPanel](#guipanel)
  - [GuiListPanel](#guilistpanel)
  - [GuiListPage](#guilistpage)
  - [GuiScrollPane](#guiscrollpane)
  - [GuiScrollBar](#guiscrollbar)
  - [GuiConfigurableButton](#guiconfigurablebutton)
- [Utilities](#-utilities)
  - [ItemStackBuilder](#itemstackbuilder)
  - [InputResult](#inputresult)
  - [Color & Gradient Support](#color--gradient-support)
- [API Interfaces](#-api-interfaces)
- [Advanced Usage](#-advanced-usage)
  - [GUI Grouping](#gui-grouping)
  - [Live Updating Components](#live-updating-components)
  - [Nested Components](#nested-components)
  - [Renderer Metadata](#renderer-metadata)
- [License](#-license)

---

## ✨ Features

- **Component-Based Architecture** — Build UIs by composing reusable components (buttons, panels, inputs, etc.).
- **Interactive Buttons** — Standard buttons, toggle/check buttons, option selectors, and configurable buttons with built-in configuration GUIs.
- **Text Input via Chat** — Prompt players to type input in chat with customizable titles, subtitles, validation, and error messages.
- **List Text Input** — Collect multiple text entries from a player, displayed as lore lines on a button.
- **Item Chooser** — Let players select an item from any inventory as a form input.
- **Editable Slots** — Inventory slots where players can place, pick up, swap, split, and stack items with full left-click, right-click, and shift-click support.
- **Panels & Nesting** — Group components into panels with their own coordinate systems; panels can be nested arbitrarily.
- **Pagination (GuiListPage)** — Automatically paginate large lists of components with configurable next/previous buttons.
- **Vertical Scrolling (GuiScrollPane)** — Scroll through components vertically with up/down buttons.
- **Horizontal Scrolling (GuiScrollBar)** — Scroll through components horizontally with left/right buttons.
- **Live Component Updates** — Schedule periodic async re-renders for individual components with automatic cleanup when no viewers remain.
- **GUI Grouping** — Group multiple GUI instances by UUID for synchronized repaint and close-all operations.
- **Color & Gradient Support** — Full support for legacy `&` color codes, hex colors (`&#FF0000`), and gradient text (`<gradient:#FF0000>text<#00FF00>`).
- **NBT Metadata** — Attach typed NBT metadata (String, Integer, Double, Boolean, Long, Float, ItemStack) to rendered items via the renderer.
- **Auto-Cleanup** — GUIs and update tasks are automatically cleaned up when no players are viewing.
- **Item Return on Close** — Editable slots can optionally return items to the player's inventory (or drop them) when the GUI is closed.
- **Backward Compatibility** — Supports Minecraft versions from 1.8 through the latest (including 26.x) via modular version adapters.
- **Folia Support** — Compatible with Folia servers through dedicated scheduling adapters.

---

## 📋 Requirements

### Server
| Requirement | Version |
|---|---|
| Minecraft | 1.8 — Latest (including 26.x) |
| Server Platform | Spigot, Paper, Folia, or any Bukkit-compatible fork |

### Plugin
| Requirement | Version |
|---|---|
| Java | 17+ |

---

## 📦 Installation

### Maven

Add the JitPack repository:

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

Add the dependency:

```xml
<dependency>
    <groupId>com.github.downnfalls</groupId>
    <artifactId>GUIAPI</artifactId>
    <version>VERSION</version>
    <scope>provided</scope>
</dependency>
```

### Gradle (Groovy)

```groovy
repositories {
    mavenCentral()
    maven { url 'https://jitpack.io/' }
}

dependencies {
    compileOnly 'com.github.downnfalls:GUIAPI:VERSION'
}
```

### Gradle (Kotlin DSL)

```kotlin
repositories {
    mavenCentral()
    maven("https://jitpack.io/")
}

dependencies {
    compileOnly("com.github.downnfalls:GUIAPI:VERSION")
}
```

### plugin.yml

Register GUIAPI as a dependency in your `plugin.yml`:

```yml
depend: [GUIAPI]
```

> Replace `VERSION` with the latest release tag from [JitPack](https://jitpack.io/#downnfalls/GUIAPI).

---

## 🚀 Quick Start

### 1. Initialize the API

```java
import io.downn_falls.libs.guiapi.GUIAPI;
import io.downn_falls.libs.guiapi.core.GUILibs;

public class MyPlugin extends JavaPlugin {
    private GUILibs guiLibs;

    @Override
    public void onEnable() {
        guiLibs = GUIAPI.get(this);
        guiLibs.register(); // Registers all internal event listeners
    }
}
```

### 2. Create and Open a GUI

```java
GUI myGui = guiLibs.createGUI("&aMy Cool Menu", 3); // 3 rows = 27 slots

// Add components...
GuiButton button = new GuiButton(myGui, "hello", 13);
button.setDisplayItem(guiLibs.getItemBuilder(Material.DIAMOND, 1).setDisplayName("&bClick Me!").build());
button.addListener((id, event) -> {
    event.getWhoClicked().sendMessage("Hello, World!");
    return true;
});

myGui.addComponent(button);
myGui.open(player);
```

---

## 🏗️ Core Concepts

### GUIAPI & GUILibs

**`GUIAPI`** is the main plugin class and entry point. Use the static `GUIAPI.get(Plugin)` method to obtain a `GUILibs` instance bound to your plugin.

**`GUILibs`** provides:

| Method | Description |
|---|---|
| `register()` | Registers the internal `GuiListener` with Bukkit's event system. **Must be called once in `onEnable()`.** |
| `createGUI(String title, int rows)` | Creates a new `GUI` instance with the given title and row count. The title supports color codes. |
| `getPlugin()` | Returns the owning plugin instance. |
| `getAdapter()` | Returns the `VersionAdapter` for the current server version. |
| `getItemBuilder()` | Creates an `ItemStackBuilder` with default Stone × 1. |
| `getItemBuilder(Material)` | Creates an `ItemStackBuilder` with the given material × 1. |
| `getItemBuilder(Material, int)` | Creates an `ItemStackBuilder` with the given material and amount. |
| `getItemBuilder(ItemStack)` | Creates an `ItemStackBuilder` by cloning an existing item. |
| `getPlayerHeadBuilder(OfflinePlayer)` | Creates an `ItemStackBuilder` for a player head item. |

> **Important:** Always use `guiLibs.getItemBuilder(...)` to create `ItemStackBuilder` instances. The builder requires a `VersionAdapter` internally for cross-version color and model data support.

---

### GUI

The `GUI` class represents a single inventory-based UI. It holds components, manages the inventory lifecycle, and handles update scheduling.

#### Constructor

```java
// Via GUILibs (recommended):
GUI gui = guiLibs.createGUI("&eTitle", 6); // 6 rows = 54 slots
```

- `title` — Inventory title. Supports `&` color codes, hex colors, and gradients.
- `rows` — Number of rows (1–6). The inventory size is `rows × 9`.

#### Methods

| Method | Return | Description |
|---|---|---|
| `open(Player player)` | `void` | Creates the inventory, renders all components, and opens the GUI for the player. |
| `addComponent(GuiComponent)` | `void` | Registers a top-level component with the GUI. |
| `getComponents()` | `Map<String, GuiComponent>` | Returns all registered top-level components, keyed by component ID. |
| `repaint()` | `void` | Re-renders all components into the current inventory. Clears and restarts updaters. |
| `repaintAll()` | `void` | Calls `repaint()` on all GUIs sharing the same group UUID. |
| `closeAll()` | `void` | Closes the inventory for all viewers across all GUIs in the same group. |
| `revalidate()` | `void` | Re-registers this GUI in the global GUI map (needed after reopening an existing inventory). |
| `getInventory()` | `Inventory` | Returns the underlying Bukkit `Inventory`, or `null` if not yet opened. |
| `getUUID()` | `UUID` | Returns the unique identifier for this GUI instance. |
| `setEditable(boolean)` | `void` | Enables/disables editable mode. Automatically set to `true` when a `GuiEditableSlot` is rendered. |
| `isEditable()` | `boolean` | Returns whether the GUI currently allows item manipulation. |
| `getEditableList()` | `List<Editable>` | Returns all editable components (recursively, including nested panels). |
| `getGroupUUID()` | `UUID` | Returns the group UUID (defaults to the GUI's own UUID). |
| `setGroupUUID(UUID)` | `void` | Sets the group UUID for synchronized operations across multiple GUIs. |
| `setTitle(String)` | `void` | Updates the GUI title. Supports color codes. |
| `addUpdater(GuiComponent, long)` | `void` | Schedules a periodic async re-render for a component. Automatically cancels when no viewers remain. |
| `clearUpdater()` | `void` | Cancels all scheduled update tasks. |
| `getGuiLibrary()` | `GUILibs` | Returns the `GUILibs` instance associated with this GUI. |

---

### GuiRenderer

`GuiRenderer` is the rendering engine responsible for placing items into the inventory. It supports **nested coordinate systems** (via parent renderers), so components inside panels render relative to the panel's position.

#### Key Methods

| Method | Description |
|---|---|
| `setSlot(int slot, ItemStack item)` | Places an item at the given slot, adjusted for parent offsets. Automatically injects NBT metadata. |
| `addMetadata(String key, T value)` | Attaches typed NBT metadata to all items rendered through this renderer. Supports `String`, `Integer`, `Double`, `Boolean`, `Long`, `Float`, and `ItemStack`. |
| `getInventory()` | Returns the underlying Bukkit `Inventory`. |
| `getParent()` | Returns the parent renderer (for nested components), or `null` for top-level. |

#### How Slot Calculation Works

The renderer converts local component slots into absolute inventory slots using:

```
finalRow    = componentRow    + parentRow    - 1
finalColumn = componentColumn + parentColumn - 1
absoluteSlot = (finalRow × parentMaxColumn) - parentMaxColumn + finalColumn - 1
```

This allows panels to act as independent coordinate spaces.

---

### GuiComponent (Base Class)

All UI components extend `GuiComponent`. It provides the shared foundation for rendering, identification, parenting, and live updates.

#### Constructor

```java
// Used internally by subclasses
GuiComponent(GUI gui, String id, int slot, int row, int column)
```

- `gui` — The parent GUI instance.
- `id` — Unique identifier for this component within its parent.
- `slot` — The starting slot index for rendering.
- `row` / `column` — The component's grid dimensions (e.g., a button is 1×1, a panel could be 3×5).

#### Methods

| Method | Return | Description |
|---|---|---|
| `getId()` | `String` | Returns the component's local ID. |
| `getFullId()` | `String` | Returns the fully qualified ID including all parent IDs, joined with `.` (e.g., `panel.subpanel.button`). |
| `getParents()` | `List<GuiComponent>` | Returns the component hierarchy from root to this component. |
| `render(GuiRenderer)` | `void` | **Abstract.** Subclasses implement this to render their visual representation. |
| `getSlot()` / `setSlot(int)` | `int` / `void` | Gets/sets the component's slot position. |
| `getRow()` / `getColumn()` | `int` | Returns the component's grid dimensions. |
| `getParent()` / `setParent(GuiComponent)` | `GuiComponent` / `void` | Gets/sets the parent component (set automatically when added to a panel). |
| `getGUI()` | `GUI` | Returns the owning GUI. |
| `getUpdateInterval()` | `long` | Returns the update interval in ticks, or `-1` if not set. |
| `setUpdateInterval(long)` | `void` | Sets the periodic update interval in ticks. When `>= 0`, the component will be re-rendered automatically on that interval. |
| `getLatestRenderer()` | `GuiRenderer` | Returns the renderer used in the most recent render pass. |

---

## 🧱 Components

### GuiButton

A clickable button that displays an item and fires listeners when clicked.

```java
GuiButton button = new GuiButton(gui, "my-button", 13); // slot 13
```

#### Methods

| Method | Return | Description |
|---|---|---|
| `setDisplayItem(ItemStack)` | `GuiButton` | Sets the item displayed for this button. |
| `getDisplayItem()` | `ItemStack` | Returns the current display item. |
| `addListener(BiFunction<String, InventoryClickEvent, Boolean>)` | `GuiButton` | Adds a click listener. The function receives the component ID and click event, and returns a boolean. Returning `true` marks the event as "listener-cancelled" which prevents default behaviors in certain subclasses. |
| `setEnable(boolean)` | `GuiButton` | Enables or disables the button. |
| `setEnable(boolean, boolean update)` | `GuiButton` | Enables/disables and optionally triggers a GUI repaint. |
| `isEnable()` | `boolean` | Returns whether the button is currently enabled. |
| `setNotEnableButton(ItemStack)` | `GuiButton` | Sets the item displayed when the button is disabled (default: red stained glass pane). |
| `whenUpdate(Runnable)` | `GuiButton` | Registers a callback that runs before each render — useful for dynamically updating the display item. |
| `isListenerCancel()` | `boolean` | Returns `true` if the last listener invocation returned `true`. |

#### Example

```java
GuiButton submit = new GuiButton(gui, "submit", 22);
submit.setDisplayItem(
    guiLibs.getItemBuilder(Material.EMERALD_BLOCK, 1)
        .setDisplayName("&aSubmit")
        .addLore("", "&7Click to confirm your selection.")
        .build()
);
submit.addListener((id, event) -> {
    Player player = (Player) event.getWhoClicked();
    player.sendMessage("§aSubmission received!");
    player.closeInventory();
    return true;
});

gui.addComponent(submit);
```

---

### GuiCheckButton

A toggle button that alternates between `true` and `false` states on each click. Uses the `{value}` placeholder in the display item's lore to show the current state.

**Extends:** `GuiButton`

```java
GuiCheckButton toggle = new GuiCheckButton(gui, "pvp-toggle", 13);
```

#### Methods

| Method | Return | Description |
|---|---|---|
| `isCheck()` | `boolean` | Returns the current toggle state. |
| `setCheck(boolean)` | `void` | Manually sets the toggle state. |
| `setEnableFormat(String)` | `void` | Sets the display text when checked/true (default: `&aTrue`). Supports color codes. |
| `setDisableFormat(String)` | `void` | Sets the display text when unchecked/false (default: `&cFalse`). Supports color codes. |

#### Behavior
- **Left-click** toggles the state and repaints the GUI.
- After toggling, any registered listeners (from `GuiButton.addListener`) are also called.

#### Example

```java
GuiCheckButton pvpToggle = new GuiCheckButton(gui, "pvp", 13);
pvpToggle.setDisplayItem(
    guiLibs.getItemBuilder(Material.IRON_SWORD, 1)
        .setDisplayName("&ePVP Mode")
        .addLore("&7Status: {value}")
        .build()
);
pvpToggle.setEnableFormat("&aEnabled");
pvpToggle.setDisableFormat("&cDisabled");
pvpToggle.addListener((id, event) -> {
    // pvpToggle.isCheck() returns the NEW state after toggling
    return false;
});
```

---

### GuiOptionButton

A button that cycles through a predefined list of options on each click. All options are displayed in the item's lore with the selected one highlighted.

**Extends:** `GuiButton`

```java
GuiOptionButton options = new GuiOptionButton(gui, "difficulty", 13);
```

#### Methods

| Method | Return | Description |
|---|---|---|
| `setDisplayItem(ItemStack)` | `GuiOptionButton` | Sets the base display item. |
| `addOption(String value, String display)` | `GuiOptionButton` | Adds a selectable option. `value` is the internal key; `display` is the shown text. |
| `getSelectedOption()` | `String` | Returns the `value` key of the currently selected option. |
| `setOptionFormat(String)` | `GuiOptionButton` | Sets the format for unselected options (default: `&7 • {option}`). Use `{option}` as the placeholder. |
| `setSelectedOptionFormat(String)` | `GuiOptionButton` | Sets the format for the selected option (default: `&f  ► {option}`). |

#### Behavior
- **Left-click** cycles to the next option (wraps around).
- If a registered listener returns `true` (listener-cancelled), the option will **not** cycle.

#### Example

```java
GuiOptionButton difficulty = new GuiOptionButton(gui, "difficulty", 22);
difficulty.setDisplayItem(
    guiLibs.getItemBuilder(Material.COMPARATOR, 1)
        .setDisplayName("&eDifficulty")
        .build()
);
difficulty.addOption("easy", "&aEasy");
difficulty.addOption("normal", "&eNormal");
difficulty.addOption("hard", "&cHard");
difficulty.addOption("nightmare", "&4Nightmare");

difficulty.addListener((id, event) -> {
    String selected = difficulty.getSelectedOption(); // "easy", "normal", etc.
    return false; // return true to prevent cycling
});
```

---

### GuiTextInput

A button that, when clicked, closes the GUI and prompts the player to type text in chat. The input is validated and the GUI is reopened with the new value.

Uses the `{text}` placeholder in the display item's lore to show the current text value.

**Extends:** `GuiButton`

```java
GuiTextInput input = new GuiTextInput(gui, "name-input", 10);
```

#### Methods

| Method | Return | Description |
|---|---|---|
| `setDisplayItem(ItemStack)` | `GuiTextInput` | Sets the display item. Use `{text}` in lore for the current value. |
| `setDefaultInput(String)` | `GuiTextInput` | Sets the default/fallback text shown when no input has been provided. |
| `getDefaultInput()` | `String` | Returns the default input text. |
| `getText()` | `String` | Returns the current text, or the default input if no text has been set. |
| `setText(String)` | `GuiTextInput` | Manually sets the text value. |
| `setText(String, boolean update)` | `GuiTextInput` | Sets the text and optionally triggers a GUI repaint. |
| `setEditTitle(String)` | `GuiTextInput` | Sets the title shown to the player during input (default: `&eInput Text`). |
| `setEditSubTitle(String)` | `GuiTextInput` | Sets the subtitle shown during input (default: `&fInput the text in the chat message`). |
| `setInvalidInputMessage(String)` | `void` | Sets the error message sent when validation fails (default: `&cInvalid input format!`). |
| `setWhenInput(Function<AsyncPlayerChatEvent, InputResult>)` | `GuiTextInput` | Sets the input validation/processing function. Return `InputResult.SUCCESS` to accept, `InputResult.ERROR` to reject. |

#### Behavior
- **Left-click** — Closes the GUI, shows a title/subtitle prompt, and waits for chat input.
- **Right-click** — Resets the text to the default value.
- If a registered listener returns `true` (listener-cancelled), the input prompt will **not** open.

#### Example

```java
GuiTextInput nameInput = new GuiTextInput(gui, "arena-name", 13);
nameInput.setDisplayItem(
    guiLibs.getItemBuilder(Material.NAME_TAG, 1)
        .setDisplayName("&eArena Name")
        .addLore("&7Current: &f{text}", "", "&eClick to edit", "&7Right-click to reset")
        .build()
);
nameInput.setDefaultInput("Unnamed");
nameInput.setEditTitle("&eEnter Arena Name");
nameInput.setEditSubTitle("&fType the name in chat");
nameInput.setWhenInput(chatEvent -> {
    String message = chatEvent.getMessage();
    if (message.length() > 30) {
        return InputResult.error("&cName must be 30 characters or less!");
    }
    if (message.contains(" ")) {
        return InputResult.error("&cName cannot contain spaces!");
    }
    return InputResult.SUCCESS;
});
```

---

### GuiListTextInput

A button that collects **multiple text entries** from the player. Each entry is displayed as a lore line on the button. Similar to `GuiTextInput` but builds a list instead of a single value.

**Extends:** `GuiButton`

```java
GuiListTextInput listInput = new GuiListTextInput(gui, "commands", 13);
```

#### Methods

| Method | Return | Description |
|---|---|---|
| `getTexts()` | `List<String>` | Returns the list of all entered texts. |
| `addText(String)` | `void` | Adds a text entry to the list. |
| `removeText(int index)` | `void` | Removes a text entry at the given index. |
| `setLoreFormat(String)` | `void` | Sets the format for each lore entry (default: `&f- {lore}`). Use `{lore}` as the placeholder. |
| `setEditTitle(String)` | `void` | Sets the title shown during input. |
| `setEditSubTitle(String)` | `void` | Sets the subtitle shown during input. |
| `setWhenInput(Function<AsyncPlayerChatEvent, InputResult>)` | `void` | Sets the input validation function. |
| `setInvalidInputMessage(String)` | `void` | Sets the error message for invalid input. |

#### Behavior
- **Left-click** — Closes the GUI and prompts for chat input. On success, the text is added to the list.
- **Right-click** — Removes the **last** entry from the list.
- If a registered listener returns `true`, the input prompt will **not** open.

#### Example

```java
GuiListTextInput commands = new GuiListTextInput(gui, "reward-commands", 22);
commands.setDisplayItem(
    guiLibs.getItemBuilder(Material.COMMAND_BLOCK, 1)
        .setDisplayName("&eReward Commands")
        .addLore("", "&7Commands to run on completion:")
        .build()
);
commands.setLoreFormat("&8 › &f{lore}");
commands.setEditTitle("&eAdd Command");
commands.setEditSubTitle("&fType the command without /");
```

---

### GuiItemChooser

A button that lets the player **select an item from any inventory** (their own inventory, a chest, etc.) as a form input. The chosen item is displayed on the button.

**Extends:** `GuiButton`

```java
GuiItemChooser chooser = new GuiItemChooser(gui, "icon", 13);
```

#### Methods

| Method | Return | Description |
|---|---|---|
| `setDisplayItem(ItemStack)` | `GuiItemChooser` | Sets the placeholder/base display item. Its lore is appended below the chosen item's info. |
| `setChooseItem(ItemStack)` | `GuiItemChooser` | Manually sets the chosen item. |
| `setChooseItem(ItemStack, boolean update)` | `GuiItemChooser` | Sets the chosen item and optionally triggers a repaint. |
| `getChooseItem()` | `ItemStack` | Returns the currently chosen item, or `null`. |
| `setChooseTitle(String)` | `GuiItemChooser` | Sets the title shown during item selection (default: `&bChoose Item`). |
| `setChooseSubTitle(String)` | `GuiItemChooser` | Sets the subtitle shown during selection (default: `&fClick any item to choose`). |

#### Behavior
- **Left-click** — Shows a title/subtitle prompt. The next item the player clicks in **any** inventory becomes the chosen item.
- **Right-click** — Clears the chosen item (resets to `null`).
- If a registered listener returns `true`, the chooser prompt will **not** open.

#### Example

```java
GuiItemChooser iconChooser = new GuiItemChooser(gui, "shop-icon", 13);
iconChooser.setDisplayItem(
    guiLibs.getItemBuilder(Material.ITEM_FRAME, 1)
        .setDisplayName("&eShop Icon")
        .addLore("&7Click to select an icon", "&7Right-click to clear")
        .build()
);
iconChooser.setChooseTitle("&bSelect an Icon");
iconChooser.setChooseSubTitle("&fClick any item in your inventory");
```

---

### GuiEditableSlot

An inventory slot where players can **place, pick up, swap, split, and stack items** — just like a real inventory slot. Supports full left-click, right-click, and shift-click interactions.

**Implements:** `Editable`, `Clickable`

```java
GuiEditableSlot slot = new GuiEditableSlot(gui, "input-slot", 22);
```

#### Methods

| Method | Return | Description |
|---|---|---|
| `setDisplayItem(ItemStack)` | `GuiEditableSlot` | Sets the placeholder item shown when the slot is empty. |
| `getItem()` | `ItemStack` | Returns the item currently placed in the slot, or `null` if empty. |
| `setItem(ItemStack)` | `void` | Manually sets the item in the slot. |
| `clearItem()` | `void` | Clears the slot (sets item to `null`). |
| `onPut(BiFunction<Player, ItemStack, Boolean>)` | `void` | Sets a callback that fires when an item is being placed. Return `true` to **cancel** the action. The `ItemStack` parameter is the **resulting** item that would be in the slot. |
| `onPickup(BiFunction<Player, ItemStack, Boolean>)` | `void` | Sets a callback that fires when an item is being picked up. Return `true` to **cancel** the action. The `ItemStack` parameter is the item being taken, or `null` if the slot would become empty. |
| `setReturnItem(boolean)` | `void` | If `true` (default), the item is returned to the player's inventory when the GUI is closed. If the inventory is full, the item is dropped as an entity near the player. |
| `isReturnItem()` | `boolean` | Returns whether items will be returned on close. |
| `whenUpdate(Runnable)` | `GuiEditableSlot` | Registers a pre-render callback. |

#### Click Behavior

| Click Type | Behavior |
|---|---|
| **Left-click** (empty cursor, item in slot) | Swap: pick up the slot's item to cursor |
| **Left-click** (item on cursor, empty slot) | Place cursor item into the slot |
| **Left-click** (item on cursor, same item in slot) | Stack items together (respects max stack size) |
| **Left-click** (item on cursor, different item in slot) | Swap cursor and slot items |
| **Right-click** (empty cursor, item in slot) | Split: take half the stack to cursor |
| **Right-click** (item on cursor, empty slot) | Place 1 item from cursor into the slot |
| **Right-click** (item on cursor, same item in slot) | Add 1 item from cursor to the slot's stack |
| **Right-click** (item on cursor, different item in slot) | Swap cursor and slot items |
| **Shift-click** (item in slot) | Move the slot's item to the player's inventory |

#### Example

```java
GuiEditableSlot inputSlot = new GuiEditableSlot(gui, "sacrifice-item", 22);
inputSlot.setDisplayItem(
    guiLibs.getItemBuilder(Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1)
        .setDisplayName("&7Place an item here")
        .build()
);
inputSlot.setReturnItem(true);
inputSlot.onPut((player, item) -> {
    if (item != null && item.getType() == Material.BEDROCK) {
        player.sendMessage("§cYou cannot place bedrock here!");
        return true; // Cancel
    }
    return false; // Allow
});
inputSlot.onPickup((player, item) -> {
    return false; // Always allow pickup
});
```

---

### GuiPanel

A container component that groups child components and provides its own coordinate system. Children render relative to the panel's position.

**Implements:** `Clickable`

```java
GuiPanel panel = new GuiPanel(gui, "sidebar", 0, 6, 2); // slot 0, 6 rows × 2 columns
```

#### Constructor

```java
GuiPanel(GUI gui, String id, int slot, int row, int column)
```

- `slot` — The starting slot in the parent context.
- `row` × `column` — The panel's grid dimensions.

#### Methods

| Method | Return | Description |
|---|---|---|
| `addComponent(GuiComponent)` | `void` | Adds a child component. The child's parent is automatically set to this panel. |
| `removeComponent(String id)` | `void` | Removes a child component by its local ID. |
| `clearComponent()` | `void` | Removes all child components. |
| `getComponents()` | `Map<String, GuiComponent>` | Returns all child components. |
| `addListener(BiFunction<String, InventoryClickEvent, Boolean>)` | `void` | Adds a click listener that fires for any click within the panel. |
| `whenUpdate(Runnable)` | `GuiPanel` | Registers a pre-render callback. |

#### Example

```java
GuiPanel toolbar = new GuiPanel(gui, "toolbar", 0, 1, 9);

GuiButton btn1 = new GuiButton(gui, "btn1", 0);
btn1.setDisplayItem(guiLibs.getItemBuilder(Material.COMPASS, 1).setDisplayName("&eHome").build());

GuiButton btn2 = new GuiButton(gui, "btn2", 1);
btn2.setDisplayItem(guiLibs.getItemBuilder(Material.CHEST, 1).setDisplayName("&eInventory").build());

toolbar.addComponent(btn1);
toolbar.addComponent(btn2);

gui.addComponent(toolbar);
```

---

### GuiListPanel

A panel that renders its child components as a flat list. Components fill slots sequentially within the panel's grid area. Can optionally hide disabled buttons.

**Extends:** `GuiPanel`

```java
GuiListPanel list = new GuiListPanel(gui, "items", 0, 3, 9); // 3 rows × 9 columns = 27 slots
```

#### Methods

| Method | Return | Description |
|---|---|---|
| `setHideIfDisable(boolean)` | `void` | If `true`, disabled `GuiButton` components are skipped during rendering. |
| `hideIfDisable()` | `boolean` | Returns the current hide-if-disabled setting. |

---

### GuiListPage

A paginated list panel with automatic **next/previous page** navigation buttons. Components are distributed across pages based on the panel's grid size.

**Extends:** `GuiListPanel`

```java
GuiListPage pagedList = new GuiListPage(gui, "shop", 0, 5, 9, prevSlot, nextSlot);
```

#### Constructor

```java
GuiListPage(GUI gui, String id, int slot, int row, int column, int prevSlot, int nextSlot)
```

- `slot` — Starting slot for the content area.
- `row` × `column` — Dimensions of the content area (items per page = row × column).
- `prevSlot` — Absolute inventory slot for the "Previous Page" button.
- `nextSlot` — Absolute inventory slot for the "Next Page" button.

#### Methods

| Method | Return | Description |
|---|---|---|
| `setNextButton(ItemStack)` | `void` | Sets the item displayed for the "Next Page" button. |
| `setPrevButton(ItemStack)` | `void` | Sets the item displayed for the "Previous Page" button. |
| `setNotAvailableButton(ItemStack)` | `void` | Sets the item displayed when a navigation button is unavailable (e.g., no next page). Default: red stained glass pane. |
| `setNotAvailableComponent(ItemStack)` | `void` | Sets the filler item for empty slots on a page. Default: gray dye. |
| `getPage()` | `int` | Returns the current page number (1-based). |
| `setPage(int)` | `void` | Manually sets the page and triggers a repaint. |

#### Example

```java
GuiListPage shopPage = new GuiListPage(gui, "products", 0, 4, 9, 36, 44);
shopPage.setNextButton(
    guiLibs.getItemBuilder(Material.ARROW, 1).setDisplayName("&eNext Page →").build()
);
shopPage.setPrevButton(
    guiLibs.getItemBuilder(Material.ARROW, 1).setDisplayName("&e← Previous Page").build()
);
shopPage.setNotAvailableComponent(
    guiLibs.getItemBuilder(Material.GRAY_STAINED_GLASS_PANE, 1).setDisplayName(" ").build()
);

// Add items
for (int i = 0; i < 100; i++) {
    GuiButton item = new GuiButton(gui, "item-" + i, 0);
    item.setDisplayItem(guiLibs.getItemBuilder(Material.GOLD_INGOT, 1).setDisplayName("&eItem #" + i).build());
    shopPage.addComponent(item);
}

gui.addComponent(shopPage);
```

---

### GuiScrollPane

A vertically scrollable panel with **up/down** navigation buttons. Scrolls by one row at a time.

**Extends:** `GuiListPanel`

```java
GuiScrollPane scrollPane = new GuiScrollPane(gui, "list", 0, 4, 7, upSlot, downSlot);
```

#### Constructor

```java
GuiScrollPane(GUI gui, String id, int slot, int row, int column, int upSlot, int downSlot)
```

- `row` × `column` — The visible viewport dimensions.
- `upSlot` — Absolute slot for the "Scroll Up" button.
- `downSlot` — Absolute slot for the "Scroll Down" button.

#### Methods

| Method | Return | Description |
|---|---|---|
| `setUpButton(ItemStack)` | `void` | Sets the "Scroll Up" button item. |
| `setDownButton(ItemStack)` | `void` | Sets the "Scroll Down" button item. |
| `setNotAvailableButton(ItemStack)` | `void` | Sets the item for disabled scroll buttons. |
| `setNotAvailableComponent(ItemStack)` | `void` | Sets the filler item for empty slots. |
| `getScroll()` | `int` | Returns the current scroll position (1-based row). |

---

### GuiScrollBar

A horizontally scrollable bar with **left/right** navigation buttons. Always 1 row tall. Scrolls by one column at a time.

**Extends:** `GuiListPanel`

```java
GuiScrollBar scrollBar = new GuiScrollBar(gui, "bar", 0, 7, leftSlot, rightSlot);
```

#### Constructor

```java
GuiScrollBar(GUI gui, String id, int slot, int column, int leftSlot, int rightSlot)
```

- `column` — Number of visible columns.
- `leftSlot` — Absolute slot for the "Scroll Left" button.
- `rightSlot` — Absolute slot for the "Scroll Right" button.

#### Methods

| Method | Return | Description |
|---|---|---|
| `setLeftButton(ItemStack)` | `void` | Sets the "Scroll Left" button item. |
| `setRightButton(ItemStack)` | `void` | Sets the "Scroll Right" button item. |
| `setNotAvailableButton(ItemStack)` | `void` | Sets the item for disabled scroll buttons. |
| `setNotAvailableComponent(ItemStack)` | `void` | Sets the filler item for empty slots. |
| `getScroll()` | `int` | Returns the current horizontal scroll position (1-based). |

---

### GuiConfigurableButton

A button with a built-in **configuration GUI**. Right-clicking opens a secondary GUI where players can edit key-value pairs. Each key uses a `GuiTextInput` for editing.

Uses `{key}` placeholders in the display item's lore to show current values.

**Extends:** `GuiButton`

```java
GuiConfigurableButton configBtn = new GuiConfigurableButton(gui, "spawn-config", 13);
```

#### Methods

| Method | Return | Description |
|---|---|---|
| `addConfig(String keyId, KeyValueTemplate)` | `GuiConfigurableButton` | Registers a configurable key with its template. |
| `getValue(String key)` | `String` | Returns the current value for a key, or the default value if unset. |
| `setValue(String key, String value)` | `void` | Sets a value for a key (validated against the template). |
| `getData()` | `HashMap<String, String>` | Returns all current key-value data. |
| `getKeyTemplates()` | `HashMap<String, KeyValueTemplate>` | Returns all registered key templates. |
| `setConfigGUI(ConfigGUI)` | `void` | Replaces the default configuration GUI with a custom one. |
| `updateConfig()` | `void` | Regenerates the default configuration GUI (call after adding new keys). |

#### KeyValueTemplate

Defines the schema for a single configurable key:

```java
KeyValueTemplate template = new KeyValueTemplate("default-value");
template.setDisplay(new ItemStack(Material.PAPER)); // Icon in config GUI
template.whenDefineValue(value -> {
    // Return true if value is valid, false otherwise
    return value.matches("\\d+"); // Only accept numbers
});
```

| Method | Return | Description |
|---|---|---|
| `KeyValueTemplate(String defaultValue)` | — | Constructor with the default value. |
| `setDisplay(ItemStack)` | `KeyValueTemplate` | Sets the icon shown in the configuration GUI. |
| `getDisplay()` | `ItemStack` | Returns the display icon. |
| `getDefaultValue()` | `String` | Returns the default value. |
| `whenDefineValue(Function<String, Boolean>)` | `KeyValueTemplate` | Sets a validation function for input values. |
| `test(String)` | `boolean` | Tests a value against the validator. |

#### Behavior
- **Left-click** — Fires registered listeners (standard button behavior).
- **Right-click** — Opens the configuration GUI.

#### Example

```java
GuiConfigurableButton spawnPoint = new GuiConfigurableButton(gui, "spawn", 13);
spawnPoint.setDisplayItem(
    guiLibs.getItemBuilder(Material.ENDER_PEARL, 1)
        .setDisplayName("&eSpawn Point")
        .addLore("&7X: &f{x}", "&7Y: &f{y}", "&7Z: &f{z}")
        .addLore("", "&eLeft-click to teleport", "&7Right-click to configure")
        .build()
);

spawnPoint.addConfig("x", new KeyValueTemplate("0")
    .setDisplay(guiLibs.getItemBuilder(Material.PAPER, 1).setDisplayName("&eX Coordinate").build())
    .whenDefineValue(v -> v.matches("-?\\d+"))
);
spawnPoint.addConfig("y", new KeyValueTemplate("64")
    .setDisplay(guiLibs.getItemBuilder(Material.PAPER, 1).setDisplayName("&eY Coordinate").build())
    .whenDefineValue(v -> v.matches("-?\\d+"))
);
spawnPoint.addConfig("z", new KeyValueTemplate("0")
    .setDisplay(guiLibs.getItemBuilder(Material.PAPER, 1).setDisplayName("&eZ Coordinate").build())
    .whenDefineValue(v -> v.matches("-?\\d+"))
);
spawnPoint.updateConfig();

spawnPoint.addListener((id, event) -> {
    Player p = (Player) event.getWhoClicked();
    int x = Integer.parseInt(spawnPoint.getValue("x"));
    int y = Integer.parseInt(spawnPoint.getValue("y"));
    int z = Integer.parseInt(spawnPoint.getValue("z"));
    p.teleport(new Location(p.getWorld(), x, y, z));
    return false;
});
```

---

## 🛠️ Utilities

### ItemStackBuilder

A fluent builder for creating `ItemStack` instances with display names, lore, enchantments, item flags, custom model data, and NBT tags.

> **Important:** Do not instantiate `ItemStackBuilder` directly. Always use the factory methods on `GUILibs`:
>
> ```java
> guiLibs.getItemBuilder()                          // Default: Stone × 1
> guiLibs.getItemBuilder(Material material)          // Material × 1
> guiLibs.getItemBuilder(Material material, int amt) // Material × amount
> guiLibs.getItemBuilder(ItemStack item)             // Clone an existing item
> guiLibs.getPlayerHeadBuilder(OfflinePlayer player)  // Player head item
> ```

#### Methods

| Method | Return | Description |
|---|---|---|
| `setDisplayName(String)` | `ItemStackBuilder` | Sets the display name (supports color codes). |
| `addLore(String...)` | `ItemStackBuilder` | Appends lore lines (supports color codes). |
| `setModel(int)` | `ItemStackBuilder` | Sets the custom model data. |
| `addItemFlag(ItemFlag...)` | `ItemStackBuilder` | Adds item flags (e.g., `HIDE_ATTRIBUTES`). |
| `addEnchant(Enchantment, int level, boolean unsafe)` | `ItemStackBuilder` | Adds an enchantment. |
| `addItemTag(String key, Object value)` | `ItemStackBuilder` | Sets an NBT tag on the item. Supports `String`, `Integer`, `Double`, `Boolean`, `Long`, `Float`, `ItemStack`. |
| `build()` | `ItemStack` | Builds and returns the final `ItemStack`. |

#### Static Methods

| Method | Return | Description |
|---|---|---|
| `replaceLore(ItemStack, String... pairs)` | `ItemStack` | Creates a clone with lore placeholders replaced. Pairs are `(find, replace, find, replace, ...)`. |

#### Example

```java
ItemStack item = guiLibs.getItemBuilder(Material.DIAMOND_SWORD, 1)
    .setDisplayName("&b&lExcalibur")
    .addLore("&7A legendary sword", "", "&eDamage: &c+50")
    .addEnchant(Enchantment.DAMAGE_ALL, 5, true)
    .addItemFlag(ItemFlag.HIDE_ENCHANTS)
    .setModel(1001)
    .build();
```

---

### InputResult

Represents the result of a text input validation. Used by `GuiTextInput` and `GuiListTextInput`.

| Factory Method | Description |
|---|---|
| `InputResult.SUCCESS` | Input accepted, no custom message. The raw chat message is used as the value. |
| `InputResult.ERROR` | Input rejected, uses the default error message. |
| `InputResult.success(String message)` | Input accepted with a **custom value** (overrides the chat message). |
| `InputResult.error(String message)` | Input rejected with a **custom error message**. |

#### Methods

| Method | Return | Description |
|---|---|---|
| `getMessage()` | `String` | Returns the custom message, or `null`. |
| `isError()` | `boolean` | Returns `true` if this is an error result. |

#### Example

```java
input.setWhenInput(chatEvent -> {
    String msg = chatEvent.getMessage();
    
    try {
        int value = Integer.parseInt(msg);
        if (value < 0 || value > 100) {
            return InputResult.error("&cValue must be between 0 and 100!");
        }
        return InputResult.success(String.valueOf(value)); // Use parsed value
    } catch (NumberFormatException e) {
        return InputResult.error("&cPlease enter a valid number!");
    }
});
```

---

### Color & Gradient Support

All text processed by the API supports three color formats:

#### Legacy Color Codes
Standard Minecraft `&` codes:
```
&a = green, &b = aqua, &c = red, &e = yellow, &l = bold, &o = italic, etc.
```

#### Hex Colors
6-digit hex colors prefixed with `&#`:
```
&#FF5733 = custom orange
&#7B68EE = medium slate blue
```

#### Gradient Text
Linear color gradients across text:
```
<gradient:#FF0000>This text fades from red to blue<#0000FF>
<gradient:#FFD700>Golden to Emerald<#50C878>
```

---

## 🔌 API Interfaces

### `Clickable`

Implemented by components that respond to inventory clicks.

```java
public interface Clickable {
    void onClick(String componentId, InventoryClickEvent event);
}
```

- `componentId` — The full component ID chain (e.g., `panel.subpanel.button`).
- `event` — The Bukkit `InventoryClickEvent`.

### `Editable`

A marker interface for components that allow item manipulation (e.g., `GuiEditableSlot`). Components implementing this interface are not automatically click-cancelled by parent panels.

```java
public interface Editable { }
```

---

## 🔬 Advanced Usage

### GUI Grouping

Group multiple GUI instances together so that `repaintAll()` and `closeAll()` affect all of them:

```java
UUID sharedGroup = UUID.randomUUID();

GUI gui1 = guiLibs.createGUI("&ePlayer 1", 3);
GUI gui2 = guiLibs.createGUI("&ePlayer 2", 3);

gui1.setGroupUUID(sharedGroup);
gui2.setGroupUUID(sharedGroup);

// Now gui1.repaintAll() will also repaint gui2
// And gui1.closeAll() will close both GUIs
```

---

### Live Updating Components

Set an update interval on any component to have it periodically re-render (e.g., a clock, countdown timer, live data display):

```java
GuiButton clock = new GuiButton(gui, "clock", 4);
clock.whenUpdate(() -> {
    String time = new SimpleDateFormat("HH:mm:ss").format(new Date());
    clock.setDisplayItem(
        guiLibs.getItemBuilder(Material.CLOCK, 1)
            .setDisplayName("&e" + time)
            .build()
    );
});
clock.setUpdateInterval(20L); // Re-render every 20 ticks (1 second)
```

> **Note:** Update tasks run asynchronously and are automatically cancelled when no players are viewing the GUI.

---

### Nested Components

Components can be nested inside panels, which can be nested inside other panels. Each panel provides its own coordinate system:

```java
// Main GUI: 6 rows × 9 columns
GUI gui = guiLibs.createGUI("&eNested Example", 6);

// Outer panel: starts at slot 10, 4 rows × 7 columns
GuiPanel outer = new GuiPanel(gui, "outer", 10, 4, 7);

// Inner panel: starts at slot 0 WITHIN the outer panel, 2 rows × 3 columns
GuiPanel inner = new GuiPanel(gui, "inner", 0, 2, 3);

// Button at slot 0 WITHIN the inner panel
GuiButton btn = new GuiButton(gui, "btn", 0);
btn.setDisplayItem(guiLibs.getItemBuilder(Material.EMERALD, 1).setDisplayName("&aNested!").build());

inner.addComponent(btn);
outer.addComponent(inner);
gui.addComponent(outer);
```

The button's full ID would be: `outer.inner.btn`

---

### Renderer Metadata

Attach NBT metadata to all items rendered through a specific renderer. This is useful for passing extra data that click handlers can read:

```java
// Inside a custom component's render method:
@Override
public void render(GuiRenderer renderer) {
    renderer.addMetadata("custom-data", "my-value");
    renderer.addMetadata("index", 42);
    renderer.setSlot(0, myItem); // This item will have the NBT tags attached
}
```

Metadata types supported: `String`, `Integer`, `Double`, `Boolean`, `Long`, `Float`, `ItemStack`.

---

## 💬 Support

- **Issues & Bug Reports:** If you find a bug or have a feature request, please open an issue on the [GitHub Issues](https://github.com/downnfalls/GUIAPI/issues) page.

---

## 📄 License

This project is licensed under the **MIT License** — see the [LICENSE](LICENSE) file for details.