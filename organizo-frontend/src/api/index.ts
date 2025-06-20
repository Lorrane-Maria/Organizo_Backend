import axios from 'axios';

const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL, // ex: "http://app:8080/api"
});

// ─────────────────────────────────────────────────────────────────────────────
// Salões
// ─────────────────────────────────────────────────────────────────────────────

// Lista paginada de salões (retorna o array `content`)
export const getSalons = async (): Promise<any[]> => {
  const { data } = await api.get('/saloes?page=0&size=100');
  return data.content;
};

// Salão por ID
export const getSalonById = async (id: number): Promise<any> => {
  const { data } = await api.get(`/saloes/${id}`);
  return data;
};

// ─────────────────────────────────────────────────────────────────────────────
// Serviços
// ─────────────────────────────────────────────────────────────────────────────

// Serviços de um salão
export const getServicesBySalon = async (salonId: number): Promise<any[]> => {
  const { data } = await api.get(`/saloes/${salonId}/servicos`);
  return data.content || data;
};

// Serviço por ID
export const getServiceById = async (id: number): Promise<any> => {
  const { data } = await api.get(`/servicos/${id}`);
  return data;
};

// ─────────────────────────────────────────────────────────────────────────────
// Profissionais
// ─────────────────────────────────────────────────────────────────────────────

// Profissionais que oferecem certo serviço em determinado salão
export const getProfessionalsByService = async (
  salonId: number,
  serviceId: number
): Promise<any[]> => {
  const { data } = await api.get(
    `/saloes/${salonId}/servicos/${serviceId}/profissionais`
  );
  return data.content || data;
};

// ─────────────────────────────────────────────────────────────────────────────
// Disponibilidade e Agendamento
// ─────────────────────────────────────────────────────────────────────────────

// Datas disponíveis de um profissional
export const getAvailabilityByProfessional = async (
  profissionalId: number
): Promise<Date[]> => {
  const { data } = await api.get(
    `/agendamentos/disponibilidade?profissionalId=${profissionalId}`
  );
  // Supondo que o back devolva lista de ISO strings:
  return data.map((iso: string) => new Date(iso));
};

// Payload para criar agendamento
export interface BookingRequest {
  clienteId: number;
  profissionalId: number;
  servicoId: number;
  dataHoraAgendada: string; // ISO
}

// Cria o agendamento
export const createBooking = async (
  payload: BookingRequest
): Promise<any> => {
  const { data } = await api.post('/agendamentos', payload);
  return data;
};
