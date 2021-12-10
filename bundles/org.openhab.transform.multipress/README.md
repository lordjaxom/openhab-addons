# multipress Profile

Profile that translates raw button presses to (multiple) click or hold events.

## Simple example

Imagine having a simple thing like a home-made ESP8266 with a button attached to it that sends via MQTT the state of the button (ON or OFF). With this profile you could link that channel to an item that received the number of clicks (i.e. one, two, three or more sequences of ON, OFF events within a short period of time) or the string "HOLD" if the button was pressed and held for a specific amount of time.

## Real-world example

The Shelly Dimmer has the possibility to connect buttons to it to control the dimmed output directly. But that button is only capable of handling specific events, i.e. a short click turns the light on or off and a press and hold starts the dimming process. But I like to customize the dimmer control and add additional functions to the button, i.e. two short clicks turn on a different light or a press and hold turns off all lights in the room if the dimmed lamp is turned off and only starts the dimming process if the dimmed lamp is already turned on. To achieve that it is possible to detach the button from the lamp within the Shelly Dimmer, but then all button functions have to be programmed in OpenHAB. This profile takes away the need to count ON and OFF events within the rule engine.

_Give some details about what this binding is meant for - a protocol, system, specific device._

_If possible, provide some resources like pictures, a YouTube video, etc. to give an impression of what can be done with this binding. You can place such resources into a `doc` folder next to this README.md._

## Supported Things

_Please describe the different supported things / devices within this section._
_Which different types are supported, which models were tested etc.?_
_Note that it is planned to generate some part of this based on the XML files within ```src/main/resources/OH-INF/thing``` of your binding._

## Discovery

_Describe the available auto-discovery features here. Mention for what it works and what needs to be kept in mind when using it._

## Binding Configuration

_If your binding requires or supports general configuration settings, please create a folder ```cfg``` and place the configuration file ```<bindingId>.cfg``` inside it. In this section, you should link to this file and provide some information about the options. The file could e.g. look like:_

```
# Configuration for the Philips Hue Binding
#
# Default secret key for the pairing of the Philips Hue Bridge.
# It has to be between 10-40 (alphanumeric) characters
# This may be changed by the user for security reasons.
secret=openHABSecret
```

_Note that it is planned to generate some part of this based on the information that is available within ```src/main/resources/OH-INF/binding``` of your binding._

_If your binding does not offer any generic configurations, you can remove this section completely._

## Thing Configuration

_Describe what is needed to manually configure a thing, either through the (Paper) UI or via a thing-file. This should be mainly about its mandatory and optional configuration parameters. A short example entry for a thing file can help!_

_Note that it is planned to generate some part of this based on the XML files within ```src/main/resources/OH-INF/thing``` of your binding._

## Channels

_Here you should provide information about available channel types, what their meaning is and how they can be used._

_Note that it is planned to generate some part of this based on the XML files within ```src/main/resources/OH-INF/thing``` of your binding._

| channel  | type   | description                  |
|----------|--------|------------------------------|
| control  | Switch | This is the control channel  |

## Full Example

_Provide a full usage example based on textual configuration files (*.things, *.items, *.sitemap)._

## Any custom content here!

_Feel free to add additional sections for whatever you think should also be mentioned about your binding!_
