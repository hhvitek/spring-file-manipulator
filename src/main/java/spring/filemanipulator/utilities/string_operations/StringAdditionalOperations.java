package spring.filemanipulator.utilities.string_operations;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

public interface StringAdditionalOperations {

    String replaceWhatTo(@NotNull String input, @NotNull String replaceWhat, @NotNull String replaceWith);

    String replaceWhatRegexTo(@NotNull String input, @NotNull Pattern regexWhat, @NotNull String replaceWith);

    String squeezeWhatRegex(@NotNull String input, @NotNull Pattern regexWhat);

    String squeezeEverything(@NotNull String input);

    String squeezeWhat(@NotNull String input, char squeezeWhat);

    String generateRandomAlphanumericString(int length);
}