# FunctionalClans
FunctionalClans — this is a system that allows players to create clans and unite in them. The leader can customize the rights of clan ranks himself, it is also possible to form alliances with other clans, and the rating system allows you to compete​

# V2.0.0 - meet GUI!
### Use /c or /c menu to invoke the GUI:
![minecraft_plugin](https://github.com/OshiFugo/FunctionalClans/assets/57602929/e74f4892-dda9-410e-9031-9829861e4f4d)


# Plugin development has been temporarily suspended
The official development and support of the plugin has been temporarily frozen due to certain events in the developer's life.
However, unofficial plugin updates from craftsmen who care about the plugin may be released in the discord
https://discord.com/invite/9EHKbdTGAR

#
If you lost your config (outdated):

```yml
// config.yml

#    ███████╗██╗░░░██╗███╗░░██╗░█████╗░████████╗██╗░█████╗░███╗░░██╗░█████╗░██╗░░░░░
#    ██╔════╝██║░░░██║████╗░██║██╔══██╗╚══██╔══╝██║██╔══██╗████╗░██║██╔══██╗██║░░░░░
#    █████╗░░██║░░░██║██╔██╗██║██║░░╚═╝░░░██║░░░██║██║░░██║██╔██╗██║███████║██║░░░░░
#    ██╔══╝░░██║░░░██║██║╚████║██║░░██╗░░░██║░░░██║██║░░██║██║╚████║██╔══██║██║░░░░░
#    ██║░░░░░╚██████╔╝██║░╚███║╚█████╔╝░░░██║░░░██║╚█████╔╝██║░╚███║██║░░██║███████╗
#    ╚═╝░░░░░░╚═════╝░╚═╝░░╚══╝░╚════╝░░░░╚═╝░░░╚═╝░╚════╝░╚═╝░░╚══╝╚═╝░░╚═╝╚══════╝
#
#    ░█████╗░██╗░░░░░░█████╗░███╗░░██╗░██████╗
#    ██╔══██╗██║░░░░░██╔══██╗████╗░██║██╔════╝
#    ██║░░╚═╝██║░░░░░███████║██╔██╗██║╚█████╗░
#    ██║░░██╗██║░░░░░██╔══██║██║╚████║░╚═══██╗
#    ╚█████╔╝███████╗██║░░██║██║░╚███║██████╔╝
#    ░╚════╝░╚══════╝╚═╝░░╚═╝╚═╝░░╚══╝╚═════╝░


# The plugin is under development, the final version will be different from the Beta


gui:
  # GUI language
  # If you want to make your own do it by creating file gui_lang_<your lang code>.yml
  lang: en
  # USE IT ONLY IF gui_lang_en.yml WAS CORRUPTED.
  # Delete your language (en) file and copy the default one
  override-lang: false
  # enable gui
  active: true

# Plugin prefix
prefix: '&f[&6FunctionalClans&f] '

# Plugin language:
  # en - English.
  # ru - Russian.
lang: en



# *** SETTINGS ***

# When using the /c home command, an action is performed:
# 0 - command (/c home, /c sethome, /c removehome) disabled, 1 - teleportation, 2 - coordinates
home: 1
# If home: 1 (teleportation), then set the teleportation delay (the time after which the player teleports to the house).
# Time in seconds.
# (default: 3)
home_delay: 3
# If home: 1 (teleportation), then allow to move when teleporting to the clan house
# When setting 1 - if the player moves or takes damage, the teleport will be canceled!
# When set to 0 - the player can move and take damage, teleport will not be canceled
# (default: 1)
home_protection_tp: 1

# How much rating should I give to a player from another verified clan that is not in an alliance
# # For killing a player rating "+", for death from a player rating "-"
# (default: 5)
kill_member: 5
# At what minimum low number of ratings to stop taking it away from a member of the clan of the killed player
# (default: -50)
min_rating: -50



# *** CREATE CLAN ***
# Initial parameters when creating a clan
# *******************

# The initial amount on the balance of the treasury
cash: 0
# Initial rating of the clan
rating: 0
# Clan type: 0 - open, 1 - closed
type: 0
# Taxes, default: 10%
tax: 10
# Verification of the clan. True of False
# [!] In the next update, the verification function and the meaning of it will appear
verification: false
# Maximum number of participants when creating a clan
# Attention! If you do not intend to give the opportunity to increase the number of places in the clan,
#   then the set value will be final, and it will be impossible to change it.
max_player: 5
# If it is allowed to increase the number of seats in the clan by purchasing, this parameter will not allow you to buy above the specified
max_player_limit: 50
# Minimum and maximum length of the clan name
min_name: 3
max_name: 30
# Maximum length of the clan status
max_status: 7


# *** Clan Economy ***
# Setting up a Clan Economy
# *************************

# # If you want to disable any of the items below, set the value to -1
# For example: ( creation_price: -1 )
# The cost of creating a clan
creation_price: 100
# The cost of deleting a clan
delete_price: 1000
# The cost of sending an invitation to the clan
invite_price: 10
# Transfer of the leader
leader_price: 5000
# Rename the name of the clan
rename_price: 1000
# The ability to buy new places, increasing the number of clan members
# Attention! If the parameter is enabled, it is recommended to set max_player << max_player_limit
# For example: ( max_player: 5; max_player_limit: 50; add_max_player: 1000; count_max_player: 2)
# The example says that when creating a clan there will be 5 seats, in the future there will be +2 seats worth $ 1000 to buy more seats.
#   You can buy seats as long as max_player <= max_player_limit
# Price for adding quantity (count_max_player ) to disable add_max_player: -1
add_max_player: 5000
# How many seats are bought for the price add_max_player
count_max_player: 1



# *** PlaceholderAPI ***
# Configuring Placeholder Display
# *******************************

# Common space for all placeholders
placeholder_space: false
# Prefix for placeholders
placeholder_prefix: '['
# Suffix for placeholders
placeholder_suffix: ']'
# A list of placeholders on which the prefix and suffix are valid
# They are not covered by the placeholder_space parameter
# Specify the placeholder name in quotation marks, for example: ['fc_player_clan_name', 'fc_clan_name_uid_', 'fc_all_count_clans', 'fc_top', 'fc_stats', 'fc_my']
# %fc_top_<name/count>_<rating, members(top), kills, deaths, kdr>_<max/min>_<Place in the top>% -> fc_top
# %fc_stats_<name/count>_<rating, members(top), kills, deaths, kdr>_<max/min>_<Place in the top>% -> fc_stats
# %fc_my_<top/stats>_<count/number>_<rating, members(top), kills, deaths, kdr>_<max/min>% -> fc_my
placeholder_list: []

# If the placeholder is empty, then in place of the placeholder will output
# [EXPLANATION] For example, we use the placeholder %fc_player_clan_name% - displays the name of the clan in which the player consists,
# if the player does not belong anywhere, then outputs placeholder_null
# For example: The player is in a clan named TEST, in place of %fc_player_clan_name% -> TEST
# if the player is not a member of the clan in place of %fc_player_clan_name% -> ''
# --- I hope that I explained it clearly, if you have any questions, please contact Discord
placeholder_null: ''
# If the placeholder specified in placeholder_null_list is empty, it will output
placeholder_null_list_symbol: 'Улыбнись, эт пасхалка'
# List of placeholders for which placeholder_null_list_symbol is valid
# Example: placeholder_null_list: ['fc_player_clan_name', 'fc_clan_name_uid_', 'fc_all_count_clans', 'fc_top', 'fc_stats', 'fc_my']
placeholder_null_list: []



# *** PERMISSIONS CLAN ***
# Setting up access rights
# ************************

# Displaying a message when changing the permission for a rank (/clan settings role)
message_setrole: false

# Everything that ends with "_enable: true/false" indicates the possibility of configuring this right to the clan leader.
# Everything that ends with "_default: true/false" indicates the default state for rank 1.
# The ability to kick clan members
kick_enable: true
kick_default: false
# The ability to send an invitation to join the clan
invite_enable: true
invite_default: false
# The ability to add coins to the clan treasury
cash_add_enable: true
cash_add_default: false
# The ability to withdraw coins from the clan treasury
cash_remove_enable: true
cash_remove_default: false
# The ability to lower and raise clan members
rmanage_enable: true
rmanage_default: false
# The ability to write to the clan chat
chat_enable: true
chat_default: true
# The ability to write ads in the clan chat
msg_enable: true
msg_default: false
# Ability to send and receive requests to join the alliance [ not available, in the next update ]
alliance_add_enable: true
alliance_add_default: false
# Possibility to terminate the alliance
alliance_remove_enable: true
alliance_remove_default: false



# *** CHAT ***
# Setting up a chat
# ************************

# Displays a notification to the clan chat about the event that occurred.
# The event that occurred comes after the "_" character.
# For example, chat_leader is a notification about the change of the leader in the clan
chat_message: true
chat_social: true
chat_status: true
chat_home: true
chat_type: true
chat_leader: true
chat_rename: true
chat_invite: true
chat_kick: true
chat_addrank: true
chat_removerank: true
chat_leave: true
chat_allyadd: true
chat_allyremove: true



# ~ Rank names ~
role_1: '&7Member'
role_2: '&7Moderator'
role_3: '&7Chief Moderator'
role_4: '&7Dep'



debug: false


```

