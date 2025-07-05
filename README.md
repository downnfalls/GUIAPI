# 🧩 GUIAPI Plugin Overview
The GUIAPI plugin is a modular, reusable GUI (Graphical User Interface) framework for Spigot plugins. It allows developers to easily create, manage, and render interactive inventory-based UIs for players using components.

## 📦 Features
- ✅ Easy API for creating inventory UIs.
- ✅ Supports live component updating.
- ✅ Group GUIs together using UUID for synchronized actions.
- ✅ Editable fields support (via Editable). 
- ✅ Auto-cleans up update tasks when no players are viewing. 
- ✅ Utility-based rendering system via GuiRenderer.

---

## 📦 Requirement
Before using the GUIAPI, make sure your plugin and server environment meet the following requirements:

### ✅ Server Requirements

- **Minecraft Version**: 1.16 or higher (tested on 1.20.1)
- **Server Platform**: Spigot, Paper, or any fork compatible with the Bukkit API

### ✅ Plugin Requirements

- **Java Version**: Java 17 or higher
- **Plugin Loader**: Bukkit-compatible loader (Spigot, Paper, etc.)
- **Dependency**: 
  - [NBT-API](https://github.com/tr7zw/Item-NBT-API)

## 📦 Importing API

Add the following entries to your pom at the correct locations:

### Maven
```xml
<repositories>
...
<!-- CodeMC -->
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>
...
</repositories>
```
```xml
<dependency>
    <groupId>com.github.downnfalls</groupId>
    <artifactId>GUIAPI</artifactId>
    <version>VERSION</version>
    <scope>provided</scope>
</dependency>
```
### Gradle
```groovy
repositories {
    mavenCentral()
    maven { url 'https://jitpack.io/' }
}
```
```groovy
dependencies {
    implementation 'com.github.downnfalls:GUIAPI:VERSION'
}
```
Add the API as dependency to your plugin.yml:
```yml
depend: [GUIAPI]
```

## 📦 Getting Started

### 1. Initialize and Register the API

```java
import io.downn_falls.libs.guiapi.GUIAPI;
import io.downn_falls.libs.guiapi.GUILibs;

public class MyPlugin extends JavaPlugin {
    private GUILibs guiLibs;

    @Override
    public void onEnable() {
        guiLibs = GUIAPI.get(this);
        guiLibs.register(); // Register GUI listeners
    }
}
```

### 2. Create a GUI
```java
GUI myGui = guiLibs.createGUI("&aMy Cool Menu", 3); // 3 rows (27 slots)
```

### 3. Add a Component
#### GUI Button
```java
GuiButton button = new GuiButton(gui, "submit", 13)
        .setDisplayItem(new ItemStackBuilder(Material.GREEN_WOOL, 1).setDisplayName("Submit").build())
        .addListener((id, event) -> {
            event.getWhoClicked().sendMessage("You clicked submit!");
            return true;
        });
```
#### Editable Button
```java
GuiEditableSlot editable = new GuiEditableSlot(gui, "input-slot", 22);
editable.onPut((player, item) -> {
    player.sendMessage("Put: " + item);
    return false;
});
```
#### List Pagination
```java
GuiListPage listPage = new GuiListPage(gui, "items", 0, 5, 9, 45, 53);
listPage.setNextButton(new ItemStackBuilder(Material.ARROW).setDisplayName("Next").build());
listPage.setPrevButton(new ItemStackBuilder(Material.ARROW).setDisplayName("Previous").build());

listPage.addComponent(button);
```
#### Text Input
You can use `{text}` as placeholder in item's lore to display user input
```java
GuiTextInput input = new GuiTextInput(gui, "name-input", 10)
    .setDefaultInput("Unnamed")
    .setDisplayItem(new ItemStackBuilder(Material.NAME_TAG).setDisplayName("Edit Name").build())
    .whenInput(chatEvent -> {
        String message = chatEvent.getMessage();
        if (message.length() > 20) return InputResult.ERROR;
        // Do something with message
        return InputResult.SUCCESS;
    });
```