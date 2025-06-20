import React from 'react';

export interface Service {
  id: number;
  nome: string;
  descricao: string;
  duracaoMinutos: number;
  preco: number;
}

interface Props {
  service: Service;
  onSelect: (serviceId: number) => void;
}

export const ServiceCard: React.FC<Props> = ({ service, onSelect }) => (
  <div className="bg-white shadow-md rounded-lg p-4 hover:shadow-lg transition">
    <h2 className="text-lg font-semibold mb-2">{service.nome}</h2>
    <p className="text-sm text-gray-600 mb-4">{service.descricao}</p>
    <div className="flex justify-between items-center">
      <span className="font-medium">{service.duracaoMinutos} min</span>
      <span className="font-bold">R$ {service.preco.toFixed(2)}</span>
    </div>
    <button
      className="mt-4 w-full bg-primary text-white rounded-lg py-2 hover:bg-primary-dark"
      onClick={() => onSelect(service.id)}
    >
      Escolher
    </button>
  </div>
);
