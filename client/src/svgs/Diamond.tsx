import React from 'react'

export default function (props: { color: string }) {
  // == Props ================================
  const { color } = props

  // == Computed Props =======================

  // == States ===============================

  // == Lifecycle ============================

  // == Functions ============================

  // == Actions ==============================

  // == Template =============================
  return (
    <g>
      <path
        stroke={color}
        fill={color}
        strokeWidth={1}
        d='M 170.1155,199.8 L 177.3155,191 L 184.5155,199.4 L 177.3155,209 L 170.1155,199.8 z'
      />
      <path
        stroke={color}
        fill={color}
        strokeWidth={1}
        d='M 14.1155,52.3 L 21.3155,43.5 L 28.5155,51.9 L 21.3155,61.5 L 14.1155,52.3 z'
      />
      <path
        stroke={color}
        fill={color}
        strokeWidth={1}
        d='M 63.5,125.75 L 99.5,81.75 L 135.5,123.75 L 99.5,171.75 L 63.5,125.75 z'
      />
    </g>
  )
}