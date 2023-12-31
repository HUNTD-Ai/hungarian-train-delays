import ReactDOM from 'react-dom/client';
import './index.css';
import { createBrowserRouter, RouterProvider } from 'react-router-dom';
import Layout from './layout.tsx';
import HomePage from './pages/home-page.tsx';
import StatisticsPage from './pages/statistics-page.tsx';
import AboutPage from './pages/about-page.tsx';
import TimetablePage from './pages/timetable-page.tsx';

const router = createBrowserRouter([
  {
    path: '/',
    element: <Layout />,
    children: [
      {
        index: true,
        element: <HomePage />,
      },
      {
        path: 'statistics',
        element: <StatisticsPage />,
      },
      {
        path: 'about',
        element: <AboutPage />,
      },
      {
        path: 'timetable',
        element: <TimetablePage />,
      },
      {
        path: '*',
        element: <HomePage />,
      },
    ],
  },
]);

ReactDOM.createRoot(document.getElementById('root')!).render(
  <RouterProvider router={router} />,
);
