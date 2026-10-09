# Propuesta nueva de seguridad FIN-001

No se recuperaron reglas originales; este archivo NO es configuración original ni está desplegado.
El proyecto auténtico firebase.json permanece intacto. Copiar sólo al laboratorio aislado para comprobar.
Los datos privados son del propietario; temporadas/rating/Merit son administrados mediante Functions.
Registro inicial conserva exactamente los valores de Users; no se autorizan concesiones del cliente.
La liberación de sesión tras rechazo conserva el contrato actual. Registro de nombre es atómico.
Las colecciones retiradas/históricas no se borran ni se habilitan al cliente.

La eliminación de cuenta cliente anterior queda DENEGADA: borraba perfil/índice sin ciclo Auth
y permitía recrear saldo inicial. Settings informa error con esta propuesta. Hace falta
un ciclo Auth confiable y su política; no consta una retirada/exclusión aprobada de
esa función. No se ejecutó eliminación de datos reales.
Esta propuesta restringe las escrituras nuevas; no redefine fallbacks de lectura históricos.
La admisión de palabras/contexto y antiabuso requieren más autoridad del servidor que estas reglas.
Pruebas REST con tokens genuinos del emulador y recorridos SDK Android documentan su alcance.

Ranked DELETE directo denegado: el callable nuevo deleteSubmission verifica owner,
estado, createTime auténtico y ACK/liquidación en TX. Histórico preactivación conserva
el contrato sin backfill; no elimina assignments/miembros ni modifica economía.
Prueba roja REST200→verde403 y16 SDK/HTTP PASS en FIN-002.
