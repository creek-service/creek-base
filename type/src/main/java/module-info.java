/** Module containing types used by many other Creek modules */
module creek.base.type {
    requires transitive creek.base.annotation;

    exports org.creekservice.api.base.type;
    exports org.creekservice.api.base.type.config;
    exports org.creekservice.api.base.type.temporal;
    exports org.creekservice.api.base.type.json;
}
