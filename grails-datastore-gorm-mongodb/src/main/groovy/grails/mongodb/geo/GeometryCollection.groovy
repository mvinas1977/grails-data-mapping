/* Copyright (C) 2014 SpringSource
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package grails.mongodb.geo

import groovy.transform.CompileStatic
import org.grails.datastore.gorm.mongo.geo.GeoJSONType

/**
 * Represents a GeoJSON GeometryCollection. See http://geojson.org/geojson-spec.html#geometry-collection
 *
 * @author Graeme Rocher
 * @since 3.0
 */

class GeometryCollection extends ArrayList<GeoJSON> implements GeoJSON {
    @Override
    List<? extends Object> asList() {
        collect() { GeoJSON current ->
            GeoJSONType.convertToGeoJSON(
                    (Shape)current)
        }
    }

    static GeometryCollection valueOf(List geometries) {

        def col = new GeometryCollection()
        def classLoader = GeometryCollection.classLoader
        for(geo in geometries) {
            if(geo instanceof Map) {
                String type = geo.type?.toString()
                def coordinates = geo.coordinates
                if(type && coordinates) {
                    col << classLoader.loadClass("grails.mongodb.geo.$type").valueOf(coordinates)
                }
            }
        }
        return col
    }
}
