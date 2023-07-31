package com.chat.manager.parser

import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeParseException

fun String.getCommand() = replace("@", SPACE).substringBefore(SPACE)
fun String.parseDate() = try { LocalDate.parse(this.split(SPACE)[1]) } catch (e: DateTimeParseException) { parseTime() }
fun String.parseTime() = try { LocalTime.parse(this.split(SPACE)[1]) } catch (e: DateTimeParseException) { try { LocalTime.parse(this.split(SPACE)[2]) } catch (e: DateTimeParseException) { null }}
fun String.parseLink() = this.split(SPACE).last()

private const val SPACE = " "