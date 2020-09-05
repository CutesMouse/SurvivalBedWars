package com.cutesmouse.airplane.events;

public abstract class Event {
    private boolean timered = false;
    private boolean toggled = false;
    public abstract void toggleTask();
    public abstract String getTimerName(int secTillToggle);
    public abstract String getSidebarName();
    public abstract String ToggleText();
    public abstract int ToggleTimeInTicks();
    public boolean shouldToggle(int tick) {
        boolean b = ToggleTimeInTicks() <= tick;
        if (b && timered) toggled = true;
        return b;
    }
    public boolean shouldTimer(int tick) {
        boolean b = shouldToggle(tick + 20 * 10);
        if (b) timered = true;
        return b;
    }
    public boolean hadTimered() {
        return timered;
    }
    public boolean hadToggled() {
        return toggled;
    }
    public boolean needTimer() {
        return true;
    }
}
