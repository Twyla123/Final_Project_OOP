# Evite-style event manager (Java)

Final project for ITP 265 (Object-Oriented Programming), University of Southern California, Spring 2023. A console application in the spirit of Evite: users register as hosts or guests, hosts create events of several types, guests are invited, accept, or request to join, and everything is read from and written back to CSV files between sessions.

## What it shows

- **Two inheritance hierarchies.** `model/people`: `User` → `Guest` → `Host`. `model/party`: the abstract `Event` with concrete `Birthday`, `Wedding`, `Graduation`, `Anniversary`, `GiftExchange` and `Fancy`, plus the `EventType` enum and the `Gift` interface implemented by hosts.
- **A shared interface for persistence.** `User_Party` is implemented by both people and events so `FileProcessor` can load one mixed collection from `user.csv` and `party.csv` and hand each object back to the right side of the program.
- **Model / view / controller layout.** `controller/Evite.java` holds the state (logged-in user, all users, events by host, the event being edited); `EviteMenu` and `PartySteps` drive the menu flow; `view/UI`, `UIPopUp` and `BFF` handle console input and output.
- **Host workflow.** Create an event, set theme, location, date, age limit and whether it is open; approve or reject guests who asked to join; keep a list of events hosted.

## Layout

```
src/
  controller/   Evite (state and start-up), EviteMenu, PartySteps, setup/FileProcessor + the two CSV files
  model/party/  Event (abstract), Birthday, Wedding, Graduation, Anniversary, GiftExchange, Fancy, EventType, Gift, User_Party
  model/people/ User, Guest, Host
  view/         UI, UIPopUp, BFF
```

The `.java.html` files at the repository root are IntelliJ's HTML export of the same sources, kept because the original submission used them; `src/` holds the actual `.java` files.

## Run

Open `src/` as a Java project in IntelliJ IDEA (or compile with `javac` from `src/`) and run `controller.Evite`. The two CSV files under `controller/setup/` provide starting users and events.

## Changelog

- **2026-09-11** Added the `.java` sources under `src/` (previously only the HTML export was in the repository) and rewrote this README.
