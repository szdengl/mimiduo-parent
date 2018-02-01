/**
 * Nconverge Boot JS.
 * 
 */
var NB = NB || {};

NB.namespace = function(namespace) {
    var nsparts = namespace.split(".");
    var parent = window;

    if (nsparts[0] === "NB") {
        nsparts = nsparts.slice(1);
        parent = NB;
    }

    for (var i = 0; i < nsparts.length; i++) {
        var partname = nsparts[i];
        if (typeof parent[partname] === "undefined") {
            parent[partname] = {};
        }

        parent = parent[partname];
    }

    return parent;
};

// alias namespace
NB.ns = NB.namespace;
