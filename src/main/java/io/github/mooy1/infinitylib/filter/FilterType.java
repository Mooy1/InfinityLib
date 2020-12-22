package io.github.mooy1.infinitylib.filter;

public enum FilterType {
    
    /**
     * ignores amount in each filter
     */
    IGNORE_AMOUNT {
        @Override
        public boolean filter(int recipe, int input) {
            return true;
        }

        @Override
        public boolean filter(int[] recipe, int[] input) {
            return true;
        }
    },

    /**
     * the input filter must have at least the amount of the recipe filter
     */
    MIN_AMOUNT {
        @Override
        public boolean filter(int recipe, int input) {
            return recipe <= input;
        }

        @Override
        public boolean filter(int[] recipe, int[] input) {
            for (int i = 0 ; i < recipe.length ; i++) {
                if (recipe[i] > input[i]) return false;
            }
            return true;
        }
    },

    /**
     * the filters must have equal amounts
     */
    EQUAL_AMOUNT {
        @Override
        public boolean filter(int recipe, int input) {
            return recipe == input;
        }

        @Override
        public boolean filter(int[] recipe, int[] input) {
            for (int i = 0 ; i < recipe.length ; i++) {
                if (recipe[i] != input[i]) return false;
            }
            return true;
        }
    };
    
    public abstract boolean filter(int recipe, int input);
    
    public abstract boolean filter(int[] recipe, int[] input);
    
}
