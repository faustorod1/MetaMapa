const LocationService = {
    API_URL: "https://apis.datos.gob.ar/georef/api/ubicacion",

    /**
     * Valida la ubicación y devuelve los datos legibles (Provincia, Departamento).
     * @param {number} lat
     * @param {number} lng
     * @returns {Promise<{valid: boolean, label: string|null}>}
     */
    async obtenerDatosUbicacion(lat, lng) {
        const url = `${this.API_URL}?lat=${lat}&lon=${lng}`;

        try {
            const response = await fetch(url);
            if (!response.ok) throw new Error("Error en API GeoRef");

            const data = await response.json();
            const ubi = data.ubicacion;

            if (ubi && ubi.provincia && ubi.provincia.id !== null) {

                const depto = ubi.departamento ? ubi.departamento.nombre : "";
                const prov = ubi.provincia.nombre;

                const label = depto ? `${depto}, ${prov}` : prov;

                return { valid: true, label: label };
            } else {
                return { valid: false, label: null };
            }

        } catch (error) {
            console.warn("LocationService falló:", error);
            return { valid: true, label: `${lat.toFixed(4)}, ${lng.toFixed(4)}` };
        }
    }
};