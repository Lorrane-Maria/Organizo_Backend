import React from 'react';

export interface Professional {
  id: number;
  nome: string;
  sobrenome: string;
  fotoUrl?: string;
}

interface Props {
  professional: Professional;
  onSelect: (professionalId: number) => void;
}

/**
 * ProfessionalCard
 * Exibe nome e foto do profissional, com botão de agendar
 */
export const ProfessionalCard: React.FC<Props> = ({ professional, onSelect }) => (
  <div className="bg-white shadow rounded-lg p-4 flex flex-col items-center">
    {professional.fotoUrl
      ? <img src={professional.fotoUrl} alt={`${professional.nome}`} className="w-24 h-24 rounded-full mb-2"/>
      : <div className="w-24 h-24 bg-gray-200 rounded-full mb-2"/>}
    <h3 className="text-lg font-semibold">{professional.nome} {professional.sobrenome}</h3>
    <button
      className="mt-4 px-4 py-2 bg-primary text-white rounded hover:bg-primary-dark"
      onClick={() => onSelect(professional.id)}
    >
      Agendar
    </button>
  </div>
);
