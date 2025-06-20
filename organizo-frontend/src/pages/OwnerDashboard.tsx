import React from 'react';
import { Link } from 'react-router-dom';
// import '../styles/Dashboard.css'; // Removido: Estilos agora são globais ou utilitários de index.css

/**
 * Dashboard do Dono do Salão:
 * - Visão geral e acesso rápido às funcionalidades administrativas.
 * - Utiliza classes globais de estilo definidas em `index.css`.
 */
export default function OwnerDashboard() {
  return (
    // Container principal da página, aplicando espaçamento padrão do dashboard
    <main className="container dashboard">
      {/* Cabeçalho da seção do dashboard */}
      <div className="dashboard__header">
        <h1 className="dashboard__title">Bem‑vindo, Dono do Salão!</h1>
        <p className="dashboard__subtitle">Gerencie seus serviços, equipe e veja o desempenho do seu negócio.</p>
      </div>

      {/* Seção de ações rápidas */}
      <div className="dashboard__actions">
        {/* Link estilizado como botão primário */}
        <Link to="/owner/servicos" className="btn btn--primary">
          Gerenciar Serviços
        </Link>
        {/* Link estilizado como botão secundário */}
        <Link to="/owner/profissionais" className="btn btn--secondary">
          Gerenciar Equipe
        </Link>
        {/* Adicionar mais links/ações conforme necessário (ex: Relatórios, Configurações) */}
      </div>

      {/* Aqui podem entrar outras seções do dashboard: */}
      {/* - Gráficos de desempenho */}
      {/* - Lista de próximos agendamentos */}
      {/* - Avisos importantes */}

    </main>
  );
}

