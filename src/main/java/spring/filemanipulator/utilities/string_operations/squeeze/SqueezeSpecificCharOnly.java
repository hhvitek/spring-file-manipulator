package spring.filemanipulator.utilities.string_operations.squeeze;

/**
 * Squeezes only squeezable sequences of the one specific character
 *
 * For example.
 * Input: aaXbbBBBcCCC
 * squeeze: 'a'
 * Output: aXbbBBBcCCC
 */
public class SqueezeSpecificCharOnly extends StringSqueeze {

    private final char squeezeWhat;

    public SqueezeSpecificCharOnly(char squeezeWhat) {
        super();
        this.squeezeWhat = squeezeWhat;
    }

    @Override
    protected boolean isThisCharRelevantForSqueezing(char c) {
        return c == squeezeWhat;
    }

}