package org.abreslav.models.metamodels;

import org.abreslav.models.*;

/**
 * @author abreslav
 */
public class ReferenceUtil {
    public static ReferenceValue ref(String identity) {
        return new ReferenceValue(new StringValue(identity));
    }
}
