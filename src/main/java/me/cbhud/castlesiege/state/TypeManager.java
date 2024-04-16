package me.cbhud.castlesiege.state;
import me.cbhud.castlesiege.Main;

public class TypeManager{
        private final Main plugin; // Add this field
        private Type currentState;

        public TypeManager(Main plugin) {
            this.plugin = plugin; // Initialize the field
            currentState = Type.Normal;
        }

        public void setState(Type newState) {
            this.currentState = newState;
            plugin.getScoreboardManager().updateScoreboardForAll();
        }

        public Type getState() {
            return currentState;
        }


        @Override
        public String toString(){
            return currentState.toString().substring(0, 1).toUpperCase() + currentState.toString().substring(1).toLowerCase();
        }


    }




