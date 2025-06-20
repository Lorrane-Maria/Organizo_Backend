import React, { useContext } from 'react';
import { Routes, Route, Navigate, Outlet } from 'react-router-dom';
import { AuthContext } from '../contexts/AuthContext';
import RegistroCliente from '../pages/RegistroCliente';

// Dashboards por perfil
import ClienteDashboard from '../pages/ClienteDashboard';
import ProfissionalDashboard from '../pages/ProfissionalDashboard';
import OwnerDashboard from '../pages/OwnerDashboard';
import AdminDashboard from '../pages/AdminDashboard';

// Fluxo de agendamento
import ServiceList from '../pages/ServiceList';
import ProfessionalList from '../pages/ProfessionalList';
import BookingCalendar from '../pages/BookingCalendar';
import BookingReview from '../pages/BookingReview';

// Outras
import LandingPage from '../pages/LandingPage';
import Login from '../pages/Login';
import Layout from '../components/Layout'; // Layout compartilhado

/**
 * Componente de proteção para rotas privadas.
 * Verifica se o usuário está autenticado antes de renderizar a rota.
 */
const PrivateRoute = ({ children }: { children: React.ReactNode }) => {
  const { token, loading } = useContext(AuthContext);

  if (loading) return <p>Carregando...</p>;

  // Se não houver token, redireciona para login
  if (!token) return <Navigate to="/login" replace />;

  // Se houver token, renderiza o conteúdo da rota
  return <>{children}</>;
};

/**
 * Componente de proteção para rotas específicas de perfil.
 * Verifica se o usuário tem o perfil necessário para acessar a rota.
 */
const RoleRoute = ({
  allowedRole,
  children
}: {
  allowedRole: 'CLIENTE' | 'PROFISSIONAL' | 'DONO_SALAO' | 'ADMIN';
  children: React.ReactNode
}) => {
  const { role, loading } = useContext(AuthContext);

  if (loading) return <p>Carregando...</p>;

  // Se o perfil não corresponder ao permitido, redireciona para o dashboard padrão
  if (role !== allowedRole) {
    return <Navigate to={getDefaultDashboardPath(role)} replace />;
  }

  // Se o perfil corresponder, renderiza o conteúdo da rota
  return <>{children}</>;
};

/**
 * Função auxiliar para determinar o path do dashboard padrão baseado no perfil do usuário.
 * @param userRole O perfil (role) do usuário atual.
 * @returns O path do dashboard correspondente ou '/login' como fallback.
 */
const getDefaultDashboardPath = (userRole: string | null | undefined): string => {
  switch (userRole) {
    case 'CLIENTE':
      return '/dashboard/cliente';
    case 'PROFISSIONAL':
      return '/dashboard/profissional';
    case 'DONO_SALAO':
      return '/dashboard/owner';
    case 'ADMIN':
      return '/dashboard/admin';
    default:
      return '/login'; // Fallback para login se não houver role válida
  }
};

/**
 * Componente principal de roteamento da aplicação.
 * Define rotas públicas e privadas, controlando o acesso baseado no token e no perfil do usuário.
 */
export default function AppRoutes() {
  const { loading } = useContext(AuthContext);

  // Exibe mensagem de carregamento enquanto o estado de autenticação é verificado inicialmente
  if (loading) return <p>Carregando...</p>;

  return (
    <Routes>
      {/* ==================================================================
          ROTAS PÚBLICAS (Acessíveis sem login)
          ================================================================== */}
      {/* A Landing Page é sempre acessível, independentemente do estado de autenticação */}
      <Route path="/" element={<LandingPage />} />

      {/* Página de Login - redireciona para o dashboard se já estiver logado */}
      <Route path="/login" element={<LoginRedirect />} />

      {/* Futura página de Contato */}
      <Route path="/contato" element={<p>Página de Contato (Em construção)</p>} />

            {/* Página de Registro de Cliente */}
      <Route path="/registro/cliente" element={<RegistroCliente />} />

      {/* ==================================================================
          ROTAS PRIVADAS (Requerem login/token)
          ================================================================== */}
      {/* Dashboard do Cliente */}
      <Route path="/dashboard/cliente" element={
        <PrivateRoute>
          <RoleRoute allowedRole="CLIENTE">
            <ClienteDashboard />
          </RoleRoute>
        </PrivateRoute>
      } />

      {/* Dashboard do Profissional */}
      <Route path="/dashboard/profissional" element={
        <PrivateRoute>
          <RoleRoute allowedRole="PROFISSIONAL">
            <ProfissionalDashboard />
          </RoleRoute>
        </PrivateRoute>
      } />

      {/* Dashboard do Dono do Salão */}
      <Route path="/dashboard/owner" element={
        <PrivateRoute>
          <RoleRoute allowedRole="DONO_SALAO">
            <OwnerDashboard />
          </RoleRoute>
        </PrivateRoute>
      } />

      {/* Dashboard do Admin */}
      <Route path="/dashboard/admin" element={
        <PrivateRoute>
          <RoleRoute allowedRole="ADMIN">
            <AdminDashboard />
          </RoleRoute>
        </PrivateRoute>
      } />

      {/* Rotas com Layout compartilhado */}
      <Route element={
        <PrivateRoute>
          <Layout>
            <Outlet />
          </Layout>
        </PrivateRoute>
      }>
        {/* Rotas do fluxo de agendamento */}
        <Route path="/salons/:salonId/services" element={<ServiceList />} />
        <Route path="/salons/:salonId/services/:serviceId/professionals" element={<ProfessionalList />} />
        <Route path="/book/:salonId/:serviceId/:professionalId/calendar" element={<BookingCalendar />} />
        <Route path="/book/review" element={<BookingReview />} />

        {/* Outras rotas privadas com layout compartilhado podem ser adicionadas aqui */}
      </Route>

      {/* Fallback para rotas não encontradas */}
      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  );
}

/**
 * Componente auxiliar para redirecionar da página de login para o dashboard
 * se o usuário já estiver autenticado.
 */
const LoginRedirect = () => {
  const { token, role, loading } = useContext(AuthContext);

  if (loading) return <p>Carregando...</p>;

  // Se já estiver logado, redireciona para o dashboard correspondente
  if (token) {
    return <Navigate to={getDefaultDashboardPath(role)} replace />;
  }

  // Se não estiver logado, mostra a página de login
  return <Login />;
};
