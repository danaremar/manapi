export const menuGroups = [
    {
        name: "Time",
        icon: "bi-clock",
        enable: true,
        features: [
            {
                name: "Efforts",
                enable: true,
                url: "/app/time/efforts"
            },
            {
                name: "Billing",
                enable: true,
                url: "/app/time/billing"
            },
            {
                name: "Reports",
                enable: true,
                url: "/app/time/reports"
            }
        ]
    },
    {
        name: "Tasks",
        icon: "bi-kanban",
        enable: true,
        features: [
            {
                name: "Kanban",
                enable: true,
                url: "/app/tasks/kanban"
            },
            {
                name: "Task list",
                enable: true,
                url: "/app/tasks/list"
            },
            {
                name: "Gantt",
                enable: true,
                url: "/app/tasks/gantt"
            },
        ]
    },
    {
        name: "Projects",
        icon: "bi-arrow-counterclockwise",
        enable: true,
        features: [
            {
                name: "Projects",
                enable: true,
                url: "/app/projects/projects"
            },
            {
                name: "Sprints",
                enable: true,
                url: "/app/projects/sprints"
            },
            {
                name: "Agile poker",
                enable: true,
                url: "/app/projects/poker"
            },
            {
                name: "Reports",
                enable: true,
                url: "/app/projects/reports"
            },
        ]
    },
    {
        name: "Incidents",
        icon: "bi-exclamation-circle",
        enable: true,
        url: "/app/incidents"
    }
]