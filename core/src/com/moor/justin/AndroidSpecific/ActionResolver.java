package com.moor.justin.AndroidSpecific;

/**
 * Created by justi on 15-11-2017.
 */

//Deze klas is de interface tussen LibGDX en Android. Dit zorgt ervoor dat er Android specific code geschreven kan worden.
//Via deze interface kunnen we vanuit LibGDX Android code gaan aanroepen.
public interface ActionResolver {
    public void showToast(CharSequence text);
}
