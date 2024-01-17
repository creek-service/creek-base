/** Module containing types used by many other Creek modules */
module creek.base.type {
    requires transitive creek.base.annotation;
    requires com.github.spotbugs.annotations;

    exports org.creekservice.api.base.type;
    exports org.creekservice.api.base.type.config;
    exports org.creekservice.api.base.type.json;
    exports org.creekservice.api.base.type.temporal;
    exports org.creekservice.api.base.type.schema;
}
