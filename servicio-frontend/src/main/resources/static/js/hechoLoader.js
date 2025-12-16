const API_URL = 'https://agregador-15-ma-ma.dds.apps.disilab.ar/api';

const delay = (ms) => new Promise(resolve => setTimeout(resolve, ms));

async function cargarHechos(filtros = {}, onLoteRecibido = () => {}) {
    const BATCH_SIZE = 100;
    const CONCURRENCIA_MAXIMA = 5;
    const BASE_URL = `${API_URL}/hechos/preview`;

    const params = new URLSearchParams({
        page: 0,
        size: BATCH_SIZE,
        ...filtros
    });

    try {
        console.time("CargaCompleta");

        console.log("Iniciando carga inicial de hechos...");
        const responsePagina0 = await fetch(`${BASE_URL}?${params.toString()}`);

        if (!responsePagina0.ok) throw new Error("Error cargando página 0");
        const dataPagina0 = await responsePagina0.json();
        if (dataPagina0.content && dataPagina0.content.length > 0) {
            onLoteRecibido(dataPagina0.content);
            await delay(100);
        }

        let todosLosHechos = [...dataPagina0.content];
        const totalPages = dataPagina0.totalPages;
        const totalElements = dataPagina0.totalElements;

        console.log(`Detectadas ${totalPages} páginas y ${totalElements} hechos en total.`);

        if (totalPages > 1) {
            // Iteramos desde la página 1 hasta el final, saltando de 5 en 5 (o lo que sea CONCURRENCIA_MAXIMA)
            for (let i = 1; i < totalPages; i += CONCURRENCIA_MAXIMA) {

                const promesasDelLote = [];
                const limiteLote = Math.min(i + CONCURRENCIA_MAXIMA, totalPages);

                console.log(`Cargando lote de páginas: ${i} a ${limiteLote - 1}...`);

                for (let j = i; j < limiteLote; j++) {
                    params.set("page", j.toString());
                    const url = `${BASE_URL}?${params.toString()}`;

                    promesasDelLote.push(
                        fetch(url)
                            .then(res => {
                                if (!res.ok) throw new Error(`Error en pág ${j}`);
                                return res.json();
                            })
                            .then(data => data.content)
                    );
                }
                const resultadosLote = await Promise.all(promesasDelLote);
                const hechosDelLote = resultadosLote.flatMap(data => data.content || []);
                if (hechosDelLote.length > 0) {
                    onLoteRecibido(hechosDelLote);
                    todosLosHechos.push(...hechosDelLote);
                }

                await delay(200);
            }
        }

        console.timeEnd("CargaCompleta");
        return todosLosHechos;

    } catch (error) {
        console.error("Falló la descarga:", error);
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