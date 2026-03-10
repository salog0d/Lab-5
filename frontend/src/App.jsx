import { useEffect, useMemo, useState } from 'react'
import './App.css'

const API_BASE = '/api/nodes'

const initialForm = {
  nodeId: '',
  name: '',
  description: '',
  input: '',
  output: '',
  nodeType: '',
  positionX: 0,
  positionY: 0,
  sequence: 0,
  isActive: true,
}

function normalizeNode(node) {
  return {
    nodeId: Number(node.nodeId ?? node.id ?? 0),
    name: node.name ?? '',
    description: node.description ?? '',
    input: node.input ?? '',
    output: node.output ?? '',
    nodeType: node.nodeType ?? '',
    positionX: Number(node.positionX ?? 0),
    positionY: Number(node.positionY ?? 0),
    sequence: Number(node.sequence ?? 0),
    isActive: Boolean(node.isActive ?? node.is_active ?? false),
  }
}

function extractNodes(payload) {
  if (Array.isArray(payload)) {
    return payload.map(normalizeNode)
  }

  if (payload?._embedded) {
    const embeddedCollection = Object.values(payload._embedded).find((value) =>
      Array.isArray(value),
    )
    if (Array.isArray(embeddedCollection)) {
      return embeddedCollection.map((item) => normalizeNode(item))
    }
  }

  if (payload?.nodeId || payload?.id) {
    return [normalizeNode(payload)]
  }

  return []
}

async function parseResponse(response) {
  if (!response.ok) {
    const errorText = await response.text()
    throw new Error(errorText || `Error HTTP ${response.status}`)
  }

  if (response.status === 204) {
    return null
  }

  return response.json()
}

function App() {
  const [nodes, setNodes] = useState([])
  const [formData, setFormData] = useState(initialForm)
  const [editingId, setEditingId] = useState(null)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState('')
  const [info, setInfo] = useState('')

  const isEditing = useMemo(() => editingId !== null, [editingId])

  const fetchNodes = async () => {
    setLoading(true)
    setError('')
    try {
      const response = await fetch(API_BASE)
      const payload = await parseResponse(response)
      setNodes(extractNodes(payload))
    } catch (err) {
      setError(`No se pudieron cargar los nodos: ${err.message}`)
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    fetchNodes()
  }, [])

  const handleChange = (event) => {
    const { name, value, type, checked } = event.target

    if (type === 'checkbox') {
      setFormData((current) => ({ ...current, [name]: checked }))
      return
    }

    if (type === 'number') {
      setFormData((current) => ({ ...current, [name]: Number(value) }))
      return
    }

    setFormData((current) => ({ ...current, [name]: value }))
  }

  const resetForm = () => {
    setFormData(initialForm)
    setEditingId(null)
  }

  const buildPayload = () => ({
    name: formData.name.trim(),
    description: formData.description.trim(),
    input: formData.input.trim(),
    output: formData.output.trim(),
    nodeType: formData.nodeType.trim(),
    positionX: Number(formData.positionX),
    positionY: Number(formData.positionY),
    sequence: Number(formData.sequence),
    isActive: Boolean(formData.isActive),
  })

  const handleSubmit = async (event) => {
    event.preventDefault()
    setError('')
    setInfo('')

    const payload = buildPayload()

    try {
      const url = isEditing ? `${API_BASE}/${editingId}` : API_BASE
      const method = isEditing ? 'PUT' : 'POST'

      const response = await fetch(url, {
        method,
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload),
      })

      await parseResponse(response)
      setInfo(isEditing ? 'Nodo actualizado correctamente.' : 'Nodo creado correctamente.')
      resetForm()
      await fetchNodes()
    } catch (err) {
      setError(`No se pudo guardar el nodo: ${err.message}`)
    }
  }

  const handleEdit = (node) => {
    setEditingId(node.nodeId)
    setFormData({ ...node })
    setInfo('')
    setError('')
  }

  const handleDelete = async (nodeId) => {
    setError('')
    setInfo('')

    try {
      const response = await fetch(`${API_BASE}/${nodeId}`, { method: 'DELETE' })
      await parseResponse(response)
      setInfo('Nodo eliminado correctamente.')

      if (editingId === nodeId) {
        resetForm()
      }

      await fetchNodes()
    } catch (err) {
      setError(`No se pudo eliminar el nodo: ${err.message}`)
    }
  }

  return (
    <main className="app-shell">
      <section className="panel panel-form">
        <h1>Node CRUD</h1>
        <p className="subtitle">Gestiona nodos con los campos del modelo backend.</p>

        <form onSubmit={handleSubmit} className="node-form">
          <label>
            Nombre
            <input
              name="name"
              value={formData.name}
              onChange={handleChange}
              placeholder="Webhook Entrada"
              required
            />
          </label>

          <label>
            Descripcion
            <textarea
              name="description"
              value={formData.description}
              onChange={handleChange}
              rows="3"
              placeholder="Nodo que recibe datos externos"
              required
            />
          </label>

          <div className="grid-2">
            <label>
              Input
              <input
                name="input"
                value={formData.input}
                onChange={handleChange}
                placeholder="payload"
                required
              />
            </label>

            <label>
              Output
              <input
                name="output"
                value={formData.output}
                onChange={handleChange}
                placeholder="normalizedPayload"
                required
              />
            </label>
          </div>

          <label>
            Tipo de nodo
            <input
              name="nodeType"
              value={formData.nodeType}
              onChange={handleChange}
              placeholder="TRIGGER"
              required
            />
          </label>

          <div className="grid-3">
            <label>
              Posicion X
              <input
                type="number"
                step="0.1"
                name="positionX"
                value={formData.positionX}
                onChange={handleChange}
                required
              />
            </label>

            <label>
              Posicion Y
              <input
                type="number"
                step="0.1"
                name="positionY"
                value={formData.positionY}
                onChange={handleChange}
                required
              />
            </label>

            <label>
              Secuencia
              <input
                type="number"
                name="sequence"
                value={formData.sequence}
                onChange={handleChange}
                required
              />
            </label>
          </div>

          <label className="checkbox-label">
            <input
              type="checkbox"
              name="isActive"
              checked={formData.isActive}
              onChange={handleChange}
            />
            Nodo activo
          </label>

          <div className="actions">
            <button type="submit">{isEditing ? 'Actualizar' : 'Crear'}</button>
            {isEditing && (
              <button type="button" className="secondary" onClick={resetForm}>
                Cancelar edicion
              </button>
            )}
          </div>
        </form>

        {info && <p className="message success">{info}</p>}
        {error && <p className="message error">{error}</p>}
      </section>

      <section className="panel panel-list">
        <div className="list-header">
          <h2>Nodos</h2>
          <button type="button" className="secondary" onClick={fetchNodes} disabled={loading}>
            {loading ? 'Cargando...' : 'Recargar'}
          </button>
        </div>

        {nodes.length === 0 && !loading ? (
          <p className="empty">No hay nodos registrados.</p>
        ) : (
          <ul className="node-list">
            {nodes.map((node) => (
              <li key={node.nodeId} className="node-item">
                <article>
                  <div className="node-top">
                    <h3>
                      #{node.nodeId} {node.name}
                    </h3>
                    <span className={node.isActive ? 'badge active' : 'badge inactive'}>
                      {node.isActive ? 'Activo' : 'Inactivo'}
                    </span>
                  </div>

                  <p>{node.description}</p>

                  <div className="node-meta">
                    <span>Tipo: {node.nodeType}</span>
                    <span>Input: {node.input}</span>
                    <span>Output: {node.output}</span>
                    <span>Pos: ({node.positionX}, {node.positionY})</span>
                    <span>Seq: {node.sequence}</span>
                  </div>

                  <div className="actions">
                    <button type="button" className="secondary" onClick={() => handleEdit(node)}>
                      Editar
                    </button>
                    <button type="button" className="danger" onClick={() => handleDelete(node.nodeId)}>
                      Eliminar
                    </button>
                  </div>
                </article>
              </li>
            ))}
          </ul>
        )}
      </section>
    </main>
  )
}

export default App
