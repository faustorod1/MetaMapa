const API_URL = 'https://agregador-15-ma-ma.dds.apps.disilab.ar:8080/api';

async function cargarHechos() {
    const BATCH_SIZE = 100;
    const BASE_URL = `${API_URL}/hechos/preview`;

    try {
        console.time("CargaCompleta");

        console.log("Iniciando carga inicial de hechos...");
        const responsePagina0 = await fetch(`${BASE_URL}?page=0&size=${BATCH_SIZE}`);

        if (!responsePagina0.ok) throw new Error("Error cargando página 0");

        const dataPagina0 = await responsePagina0.json();

        let todosLosHechos = [...dataPagina0.content];
        const totalPages = dataPagina0.totalPages;
        const totalElements = dataPagina0.totalElements;

        console.log(`Detectadas ${totalPages} páginas y ${totalElements} hechos en total.`);

        if (totalPages > 1) {
            const promesasRestantes = [];

            for (let i = 1; i < totalPages; i++) {
                const promesa = fetch(`${BASE_URL}?page=${i}&size=${BATCH_SIZE}`)
                    .then(res => {
                        if (!res.ok) throw new Error(`Error en página ${i}`);
                        return res.json();
                    })
                    .then(data => data.content);

                promesasRestantes.push(promesa);
            }

            console.log("Descargando páginas restantes en paralelo...");
            const resultadosRestantes = await Promise.all(promesasRestantes);

            resultadosRestantes.forEach(listaDePagina => {
                todosLosHechos = todosLosHechos.concat(listaDePagina);
            });
        }

        console.timeEnd("CargaCompleta");
        console.log(`Descarga finalizada. Total hechos en memoria: ${todosLosHechos.length}`);

        return todosLosHechos;

    } catch (error) {
        console.error("Falló la descarga de hechos:", error);
        return [];
    }
}


async function cargarHecho(id) {
    const BASE_URL = `${API_URL}/hechos/`;

    try {
        const response = await fetch(BASE_URL + id);
        const data = await response.json();
        return data;
    } catch (error) {
        console.error(`Error al cargar datos del hecho ${id}:`, error);
    }
}